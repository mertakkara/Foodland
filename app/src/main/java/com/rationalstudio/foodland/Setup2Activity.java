package com.rationalstudio.foodland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Setup2Activity extends AppCompatActivity {
    String foodId;
    private View view;
    private GetSharedPreferences getSharedPreferences;
    EditText recipe,ind;
    TextView foodName;
    ImageView foodImage;
    RecyclerView icindekilerRecyclerView,tarifrecyclerView;
    Button getRating,geri;
    FoodsDetailAdapter foodsDetailAdapter;
    RatingBar ratingBar;
    Context context;
    private String id;
    int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        foodId = getIntent().getStringExtra("id");;
        foodName = findViewById(R.id.foodName);
        foodImage = findViewById(R.id.foodImage);
        recipe = findViewById(R.id.recipe);
        ind =findViewById(R.id.ind);
        getRating = findViewById(R.id.getRating);
        ratingBar = findViewById(R.id.rating);
        geri = findViewById(R.id.geri);
        getSharedPreferences = new GetSharedPreferences(Setup2Activity.this);
        id = getSharedPreferences.getSession().getString("id",null);
geri.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Setup2Activity.this,SetupActivity.class);
        startActivity(intent);
        finish();
    }
});
        getRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = "Puanınız gönderildi.";
                b=(int)(Math.round(ratingBar.getRating()));
                //Toast.makeText(getContext(), id + " " + foodId + " " + b, Toast.LENGTH_LONG).show();
                String postUrl = "http://10.0.2.2:5000/rating";


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
                RequestQueue requestQueue = Volley.newRequestQueue(Setup2Activity.this);
                requestQueue.add(jsObjRequest2 );




            }
        });

        RequestQueue queue = Volley.newRequestQueue(Setup2Activity.this);
        String url = "http://10.0.2.2:5000/fooddetail";





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
                        ind.setText(Jobj.getString("ind"));




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
        RequestQueue requestQueue = Volley.newRequestQueue(Setup2Activity.this);
        requestQueue.add(jsObjRequest);

    }

}
