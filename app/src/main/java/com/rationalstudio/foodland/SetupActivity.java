package com.rationalstudio.foodland;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Adapters.Foods2Adapter;
import com.rationalstudio.foodland.Adapters.FoodsAdapter;
import com.rationalstudio.foodland.Models.RecipeDetailModel;
import com.rationalstudio.foodland.Models.RecipeModel;
import com.rationalstudio.foodland.RestApi.ManagerAll;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.rationalstudio.foodland.Utils.Warnings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetupActivity extends AppCompatActivity {
    private List<RecipeModel> foodModelList;
    private ChangeFragments changeFragments;
    private View view;
    private RecyclerView foodlistrecyclerView;
    private Foods2Adapter foodsAdapter;
    Button onay;
    String postUrl = "http://10.0.2.2:5000/collabcsv";
    String url = "http://10.0.2.2:5000/zero_all";
    String url2 = "http://10.0.2.2:5000/itemcsv";
    private GetSharedPreferences getSharedPreferences;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        getSharedPreferences = new GetSharedPreferences(getApplicationContext());
        id = getSharedPreferences.getSession().getString("id",null);

        //final ProgressDialog progressDialog = new ProgressDialog(SetupActivity.this);
        //progressDialog.setTitle("Yükleniyor...");
        //progressDialog.show();


        final ProgressDialog progressDialog2 = new ProgressDialog(SetupActivity.this);
        progressDialog2.setTitle("Yükleniyor...");
        progressDialog2.show();

        final ProgressDialog progressDialog3 = new ProgressDialog(SetupActivity.this);
        progressDialog3.setTitle("Yükleniyor...");
        progressDialog3.show();

         JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, postUrl, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
            progressDialog2.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest2);


        //--------------------------------------------------------------


      /*  JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        try {
            // adding some keys
            jsonobj.put("user", id);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new com.android.volley.Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
progressDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest);
*/


        /////////////////////////////////////////

        JsonObjectRequest jsObjRequest3 = new JsonObjectRequest(Request.Method.POST, url2, null, new com.android.volley.Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
        progressDialog3.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });
        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest3);





        onay = findViewById(R.id.onay);
        foodModelList = new ArrayList<>();
        foodlistrecyclerView = findViewById(R.id.foodlistrecyclerView);


        foodModelList = new ArrayList<>();
        onay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /////////////getfood



        String url2 = "http://10.0.2.2:5000/food";
        //String url2 = "http://localhost:5000/item";
        JSONObject jsonobj2; // declared locally so that it destroys after serving its purpose
        jsonobj2 = new JSONObject();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url2, null, new com.android.volley.Response.Listener<JSONObject>() {


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

                        foodModelList.add(item);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(SetupActivity.this);
        requestQueue2.add(jsObjRequest);


        foodsAdapter = new Foods2Adapter(foodModelList,SetupActivity.this);
        foodlistrecyclerView.setAdapter(foodsAdapter);
        RecyclerView.LayoutManager mng =  new GridLayoutManager(SetupActivity.this,2);
        foodlistrecyclerView.setLayoutManager(mng);








    }

}
