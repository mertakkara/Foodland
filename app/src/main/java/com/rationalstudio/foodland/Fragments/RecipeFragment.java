package com.rationalstudio.foodland.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Adapters.FoodListAdapter2;
import com.rationalstudio.foodland.Adapters.FoodsAdapter;
import com.rationalstudio.foodland.Models.RecipeDetailModel;
import com.rationalstudio.foodland.Models.RecipeModel;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.RestApi.ManagerAll;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.Warnings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeFragment extends Fragment {
    private List<RecipeDetailModel> foodModelList;
    private List<RecipeDetailModel> foodModelList2;
    private List<RecipeModel> foodModelList3;
    private ChangeFragments changeFragments;
    private View view;
    private RecyclerView foodlistrecyclerView;
    private FoodsAdapter foodsAdapter;
    private EditText searchEdt;
    private Button searchButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_recipe, container, false);
        searchButton = view.findViewById(R.id.searchButton);
        searchEdt = view.findViewById(R.id.searchEdt);
        foodModelList = new ArrayList<>();
        foodModelList2 = new ArrayList<>();
        foodModelList3 = new ArrayList<>();


        changeFragments = new ChangeFragments(getContext());
        foodModelList = new ArrayList<>();



        /////////////getfood



        String url2 = "http://10.0.2.2:5000/food";
        //String url2 = "http://localhost:5000/item";
        JSONObject jsonobj2; // declared locally so that it destroys after serving its purpose
        jsonobj2 = new JSONObject();

        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, url2, null, new com.android.volley.Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray jsonArray = response.getJSONArray("cursor");


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject Jobj = jsonArray.getJSONObject(i);


                        String image = Jobj.getString("image");

                        String name = Jobj.getString("name");
                        Integer id = Jobj.getInt("id");
                        RecipeModel item = new RecipeModel(image,name,id);

                        foodModelList3.add(item);
                        foodsAdapter.notifyDataSetChanged();






                    }







                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });
        jsObjRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                // Here goes the new timeout 3 minutes
                return 3*60*1000;
            }

            @Override
            public int getCurrentRetryCount() {
                // The max number of attempts
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
        requestQueue2.add(jsObjRequest2);

        foodlistrecyclerView = view.findViewById(R.id.foodlistrecyclerView);
        foodsAdapter = new FoodsAdapter(foodModelList3,getContext());
        foodlistrecyclerView.setAdapter(foodsAdapter);
        RecyclerView.LayoutManager mng =  new GridLayoutManager(getContext(),2);
        foodlistrecyclerView.setLayoutManager(mng);



////////////////////////searching


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postUrl = "http://10.0.2.2:5000/search";

                foodModelList3.clear();
                JSONObject data2 = new JSONObject();
                try {
                    data2.put("name",searchEdt.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsObjRequest3 = new JsonObjectRequest(Request.Method.POST, postUrl, data2, new com.android.volley.Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            JSONArray jsonArray = response.getJSONArray("cursor");


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject Jobj = jsonArray.getJSONObject(i);


                                String image = Jobj.getString("image");


                                String name = Jobj.getString("name");
                                Integer id = Jobj.getInt("id");
                                RecipeModel item = new RecipeModel(image,name,id);

                                foodModelList3.add(item);







                            }







                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        foodsAdapter.notifyDataSetChanged();


                    }
                },



                 new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("idid", "onErrorResponse: "+error);

                    }
                });
                RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                requestQueue2.add(jsObjRequest3 );
            }
        });

        return view;
    }


}
