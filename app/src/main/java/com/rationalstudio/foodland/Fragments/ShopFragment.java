package com.rationalstudio.foodland.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Adapters.ItemListAdapter;
import com.rationalstudio.foodland.Models.ItemItem;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ShopFragment extends Fragment {
    private ArrayList<ItemItem> itemList;
    private ChangeFragments changeFragments;
    private View view;
    private GetSharedPreferences getSharedPreferences;
    private RecyclerView basketrecyclerView;
    private String id;
    private ItemListAdapter itemlistAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Button addButton;
    private EditText addEdt;


    public ShopFragment() {
// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_shop, container, false);

        itemList = new ArrayList<>();

        changeFragments = new ChangeFragments(getContext());
        getSharedPreferences = new GetSharedPreferences(getActivity());
        id = getSharedPreferences.getSession().getString("id",null);
        addButton = view.findViewById(R.id.addButton);
        addEdt = view.findViewById(R.id.addEdt);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://10.0.2.2:5000/additem";





                JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
                jsonobj = new JSONObject();
                try {
                    // adding some keys
                    jsonobj.put("user_id", id);
                    jsonobj.put("name", addEdt.getText().toString());


                } catch (JSONException ex) {

                    ex.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new Response.Listener<JSONObject>() {


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
                requestQueue.add(jsObjRequest);
                addEdt.setText("");
                ChangeFragments changeFragments = new ChangeFragments(getContext());
                changeFragments.change(new ShopFragment());
            }
        });




        String url = "http://10.0.2.2:5000/getitem";
        //String url = "http://localhost:5000/getitem";





        JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        try {
            // adding some keys
            jsonobj.put("user_id", id);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("item");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject Jobj = jsonArray.getJSONObject(i);
                        String item = Jobj.getString("item");
                        Integer id = Jobj.getInt("id");

                        ItemItem item2 = new ItemItem(item,id);

                        itemList.add(item2);
                        itemlistAdapter.notifyDataSetChanged();





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


        basketrecyclerView = view.findViewById(R.id.basketrecyclerView);
        itemlistAdapter = new ItemListAdapter(itemList,getContext());
        basketrecyclerView.setAdapter(itemlistAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);

        basketrecyclerView.setLayoutManager(linearLayoutManager);






        return view;
    }



}
