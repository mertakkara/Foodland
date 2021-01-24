package com.rationalstudio.foodland.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Adapters.FoodsDetailAdapter;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


public class FoodDetailFragment extends Fragment {
    String foodId;
   private View view;
   private GetSharedPreferences getSharedPreferences;
   EditText recipe,ind,likeedt;
   TextView foodName;
   ImageView foodImage;
   RecyclerView icindekilerRecyclerView,tarifrecyclerView;
   Button getRating,getİnd;
   FoodsDetailAdapter foodsDetailAdapter;
   RatingBar ratingBar;
   Context context;
   private String id;
   float b;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        foodId = getArguments().getString("foodId");
        foodName = view.findViewById(R.id.foodName);
        foodImage = view.findViewById(R.id.foodImage);
        recipe = view.findViewById(R.id.recipe);
        likeedt = view.findViewById(R.id.likeedt);
        ind =view.findViewById(R.id.ind);
        getRating = view.findViewById(R.id.getRating);
        getİnd = view.findViewById(R.id.getİnd);
        ratingBar = view.findViewById(R.id.rating);
        getSharedPreferences = new GetSharedPreferences(getActivity());
        id = getSharedPreferences.getSession().getString("id",null);

        getİnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postUrl = "http://10.0.2.2:5000/addlist";
                //String postUrl = "http://localhost:5000/addlist";


                JSONObject data2 = new JSONObject();
                try {
                    data2.put("id", foodId);
                    data2.put("user_id",id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsObjRequest3 = new JsonObjectRequest(Request.Method.POST, postUrl, data2, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("idid", "onErrorResponse: "+error);

                    }
                });
                RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                requestQueue2.add(jsObjRequest3 );
            }
        });


       getRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = "Puanınız gönderildi.";
                b=ratingBar.getRating();
                //Toast.makeText(getContext(), id + " " + foodId + " " + b, Toast.LENGTH_LONG).show();
                String postUrl = "http://10.0.2.2:5000/rating";
                //String postUrl = "http://localhost:5000/rating";


                    JSONObject data = new JSONObject();
                    try {
                        data.put("recipe", foodId);
                        data.put("user",id);
                        data.put("rating", b);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, postUrl, data, new Response.Listener<JSONObject>() {


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
                requestQueue.add(jsObjRequest2 );




            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = "http://10.0.2.2:5000/fooddetail";
        //String url = "http://localhost:5000/fooddetail";





        JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        try {
            // adding some keys
            jsonobj.put("id", foodId);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("cursor");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject Jobj = jsonArray.getJSONObject(i);


                        foodName.setText(Jobj.getString("name"));
                        Picasso.get().load(Jobj.getString("image")).into(foodImage);
                        recipe.setText(Jobj.getString("recip"));
                        String ind2 = Jobj.getString("ind");
                        ind2= ind2.replaceAll(" ", ",");
                        ind.setText(ind2);




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


        //////////////////////////



        String url2 = "http://10.0.2.2:5000/getlike";
        //String url2 = "http://localhost:5000/getlike";





        JSONObject jsonobj2; // declared locally so that it destroys after serving its purpose
        jsonobj2 = new JSONObject();
        try {
            // adding some keys
            jsonobj2.put("id", foodId);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }

        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, url2, jsonobj2, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("cursor");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject Jobj = jsonArray.getJSONObject(i);

                        likeedt.setText(Jobj.getString("number"));





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
        requestQueue2.add(jsObjRequest2);






        return view;
    }







}








