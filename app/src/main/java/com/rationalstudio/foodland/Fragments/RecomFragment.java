package com.rationalstudio.foodland.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;


public class RecomFragment extends Fragment {

    ChangeFragments changeFragments;
    private View view;
    private GetSharedPreferences getSharedPreferences;
    private String id;
    EditText recipe,ind;
    TextView foodName;
    TextView foodName2;
    ImageView foodImage;
    ImageView foodImage2;
    LinearLayout FoodAnaLayout;
    LinearLayout FoodAnaLayout2;
    String name,image;
    String name2,image2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_recom, container, false);
        foodName = view.findViewById(R.id.foodName);
        foodName2 = view.findViewById(R.id.foodName2);
        foodImage = view.findViewById(R.id.foodImage);
        foodImage2 = view.findViewById(R.id.foodImage2);
        FoodAnaLayout = view.findViewById(R.id.FoodAnaLayout);
        FoodAnaLayout2 = view.findViewById(R.id.FoodAnaLayout2);
        changeFragments = new ChangeFragments(getContext());
        getSharedPreferences = new GetSharedPreferences(getActivity());
        id = getSharedPreferences.getSession().getString("id",null);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://10.0.2.2:5000/collab";

        JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        try {
            // adding some keys
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


                        foodName.setText(Jobj.getString("name"));
                        Picasso.get().load(Jobj.getString("image")).into(foodImage);
                        name = Jobj.getString("name");
                        image = Jobj.getString("image");




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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);

////denem
        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        String url2 = "http://10.0.2.2:5000/item";

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


                        foodName2.setText(Jobj.getString("name"));
                        Picasso.get().load(Jobj.getString("image")).into(foodImage2);
                        name2 = Jobj.getString("name");
                        image2 = Jobj.getString("image");




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
        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest2);



        FoodAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://10.0.2.2:5000/list";

                JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
                jsonobj = new JSONObject();
                try {
                    // adding some keys
                    jsonobj.put("name", name);
                    jsonobj.put("user_id", id);
                    jsonobj.put("image", image);


                } catch (JSONException ex) {

                    ex.printStackTrace();
                }
                JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("idid", "onErrorResponse: "+error);

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(jsObjRequest2);
                changeFragments.change(new HomeFragment());
            }

        });



        FoodAnaLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://10.0.2.2:5000/list";

                JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
                jsonobj = new JSONObject();
                try {
                    // adding some keys
                    jsonobj.put("name", name2);
                    jsonobj.put("user_id", id);
                    jsonobj.put("image", image2);


                } catch (JSONException ex) {

                    ex.printStackTrace();
                }
                JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("idid", "onErrorResponse: "+error);

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(jsObjRequest2);
                changeFragments.change(new HomeFragment());
            }

        });


        return view;
    }


}
