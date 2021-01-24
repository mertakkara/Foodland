package com.rationalstudio.foodland;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.rationalstudio.foodland.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.rationalstudio.foodland.Adapters.FoodsDetailAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private TextView loginText;
String username;
String password;
    static String postUrl = "http://10.0.2.2:5000/";
    //static String postUrl = "http://localhost:5000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginText = findViewById(R.id.loginText);
          loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

  public void submit(View v) {
        EditText usernameView = findViewById(R.id.loginUsername);
        EditText passwordView = findViewById(R.id.loginPassword);

        username = usernameView.getText().toString().trim();
        password = passwordView.getText().toString().trim();

        if (username.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Eksik bilgi girdiniz", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("subject", "login");
            loginForm.put("username", username);
            loginForm.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, postUrl, loginForm, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject Jobj = jsonArray.getJSONObject(i);
                        String idi = Jobj.getString("id");
                        String status = Jobj.getString("status");
                        Log.d("iasa",status);
                        if( status.contains("0") || status.equals("0")){


                            Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
                            GetSharedPreferences getSharedPreferences = new GetSharedPreferences(LoginActivity.this);
                            getSharedPreferences.setSession(username,password,idi);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            GetSharedPreferences getSharedPreferences = new GetSharedPreferences(LoginActivity.this);
                            getSharedPreferences.setSession(username,password,idi);
                            startActivity(intent);
                            finish();

                        }





                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);
                Toast.makeText(getApplicationContext(), "Kullanıcı adı veya şifre hatalı.! " , Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsObjRequest);
    }


}
