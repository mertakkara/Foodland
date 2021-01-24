package com.rationalstudio.foodland.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Adapters.FoodListAdapter;
import com.rationalstudio.foodland.Adapters.FoodListAdapter2;
import com.rationalstudio.foodland.Adapters.FoodListAdapter3;
import com.rationalstudio.foodland.Models.CursorItem;
import com.rationalstudio.foodland.Models.RecipeModel;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.SetupActivity;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.rationalstudio.foodland.Utils.Warnings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment {
    private List<CursorItem> foodModelList;
    private List<RecipeModel> foodModelList2;
    private List<RecipeModel> foodModelList3;
    private ChangeFragments changeFragments;
    private View view;
    private GetSharedPreferences getSharedPreferences;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private String id;
    private FoodListAdapter foodListAdapter;
    private FoodListAdapter2 foodListAdapter2;
    private FoodListAdapter3 foodListAdapter3;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager2;
    private LinearLayoutManager linearLayoutManager3;


    public HomeFragment() {
// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);




        foodModelList = new ArrayList<>();
        foodModelList2 = new ArrayList<>();
        foodModelList3 = new ArrayList<>();





        changeFragments = new ChangeFragments(getContext());
        getSharedPreferences = new GetSharedPreferences(getActivity());
        id = getSharedPreferences.getSession().getString("id",null);





        String url = "http://10.0.2.2:5000/foodlist";
        //String url = "http://localhost:5000/foodlist";
        JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        try {
            // adding some keys
           // int id2 = Integer.parseInt(id);
            jsonobj.put("id", id);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new com.android.volley.Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray jsonArray = response.getJSONArray("cursor");




                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject Jobj = jsonArray.getJSONObject(i);
                        String date = Jobj.getString("date");
                        String date2 = date.replaceAll(".*,", "");
                        date2 = date2.replaceAll("GMT", "");
                        date2 = date2.replaceAll("Oct", "10");

                        String image = Jobj.getString("image");
                        Integer user_id = Jobj.getInt("user_id");
                        String name = Jobj.getString("name");
                        Integer id = Jobj.getInt("id");
                        CursorItem item = new CursorItem(date2,image,user_id,name,id);

                        foodModelList.add(item);
                        foodListAdapter.notifyDataSetChanged();






                    }







                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });
        jsObjRequest.setRetryPolicy(new RetryPolicy() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        recyclerView = view.findViewById(R.id.recyclerView);
        foodListAdapter = new FoodListAdapter(foodModelList,getContext());
        recyclerView.setAdapter(foodListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);

        /////////////item



        String url2 = "http://10.0.2.2:5000/item";
        //String url2 = "http://localhost:5000/item";
        JSONObject jsonobj2; // declared locally so that it destroys after serving its purpose
        jsonobj2 = new JSONObject();
        try {
            // adding some keys
            jsonobj2.put("id", id);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, url2, jsonobj2, new com.android.volley.Response.Listener<JSONObject>() {


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

                        foodModelList2.add(item);
                        foodListAdapter2.notifyDataSetChanged();






                    }









                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });

       /* jsObjRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 3*60*1000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });*/
        jsObjRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                // Here goes the new timeout 3 minutes
                return 3*60*1000;
            }

            @Override
            public int getCurrentRetryCount() {
                // The max number of attempts
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());


        recyclerView2 = view.findViewById(R.id.recyclerView2);
        foodListAdapter2 = new FoodListAdapter2(foodModelList2,getContext());
        recyclerView2.setAdapter(foodListAdapter2);
        linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView2.setLayoutManager(linearLayoutManager2);



////////////////////////collab
      String url3 = "http://10.0.2.2:5000/collab";
      //String url3 = "http://localhost:5000/collab";
        JSONObject jsonobj3; // declared locally so that it destroys after serving its purpose
        jsonobj3 = new JSONObject();
        try {
            // adding some keys
            jsonobj3.put("id", id);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        JsonObjectRequest jsObjRequest3 = new JsonObjectRequest(Request.Method.POST, url3, jsonobj3, new com.android.volley.Response.Listener<JSONObject>() {


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
                        foodListAdapter3.notifyDataSetChanged();






                    }







                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });
        jsObjRequest3.setRetryPolicy(new RetryPolicy() {
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
        RequestQueue requestQueue3 = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
        requestQueue.add(jsObjRequest2);
        requestQueue.add(jsObjRequest3);

        recyclerView3 = view.findViewById(R.id.recyclerView3);
        foodListAdapter3 = new FoodListAdapter3(foodModelList3,getContext());
        recyclerView3.setAdapter(foodListAdapter3);
        linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView3.setLayoutManager(linearLayoutManager3);









        return view;
    }



}
