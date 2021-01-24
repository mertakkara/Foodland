package com.rationalstudio.foodland;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.rationalstudio.foodland.Fragments.RecipeFragment;
import com.rationalstudio.foodland.Fragments.RecomFragment;
import com.rationalstudio.foodland.Fragments.HomeFragment;
import com.rationalstudio.foodland.Fragments.ShopFragment;
import com.rationalstudio.foodland.Fragments.AccountFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static Context mainActivityContext;

    private ImageView exitButton,mainpageButton,recipeButton;
    private ImageView recomButton,accountButton,shopButton;
    private SharedPreferences sharedPreferences;
    private GetSharedPreferences getSharedPreferences;
    private ChangeFragments changeFragments;


    static String postUrl = "http://10.0.2.2:5000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragment();

        mainpageButton = findViewById(R.id.mainpageButton);
        recipeButton = findViewById(R.id.recipeButton);
        accountButton = findViewById(R.id.accountButton);
        shopButton = findViewById(R.id.shopButton);
        getSharedPreferences = new GetSharedPreferences(MainActivity.this);
        sharedPreferences = getSharedPreferences.getSession();

        control();
        action();


        exitButton = findViewById(R.id.exitButton);


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetSharedPreferences getSharedPreferences= new GetSharedPreferences(MainActivity.this);
                getSharedPreferences.deleteToSession();
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new RecipeFragment());
            }
        });

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new AccountFragment());
            }
        });
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new ShopFragment());
            }
        });

    }




    public void action() {
        mainpageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new HomeFragment());
            }
        });
    }

    public void item(View v) {


        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("subject","item");
            loginForm.put("food", "karniyarik");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), loginForm.toString());

        postRequest(MainActivity.postUrl, body);
    }
    public void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseTextLogin = findViewById(R.id.responseTextLogin);
                        try {
                            String loginResponseString = response.body().string().trim();
                            Log.d("LOGIN", "Response from the server : " + loginResponseString);




                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("LOGIN","hata");
                        }
                    }
                });
            }
        });
    }


    public void collab(View v) {


        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("subject","collab");
            loginForm.put("food", "karniyarik");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), loginForm.toString());

        postRequest(MainActivity.postUrl, body);
    }





    public void control() {
        if ( sharedPreferences.getString("username", null) == null && sharedPreferences.getString("password", null) == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }



    private void getFragment() {
        changeFragments = new ChangeFragments(MainActivity.this);
        changeFragments.change(new HomeFragment());
    }
}
