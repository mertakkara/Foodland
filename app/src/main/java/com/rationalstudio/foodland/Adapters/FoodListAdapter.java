package com.rationalstudio.foodland.Adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Fragments.FoodDetailFragment;
import com.rationalstudio.foodland.Fragments.HomeFragment;
import com.rationalstudio.foodland.Models.CursorItem;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

//import com.rationalstudio.foode.Fragments.FoodDetailFragment;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    List<CursorItem> list;
    Context context;

    public FoodListAdapter(List<CursorItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName,foodDate;
        CircleImageView foodImage;
        RelativeLayout FoodAnaLayout;
        Button recipeBtn,deleteBtn;

        public ViewHolder( View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
          //  foodDate = itemView.findViewById(R.id.foodDate);
            foodImage = itemView.findViewById(R.id.foodImage);
            FoodAnaLayout = itemView.findViewById(R.id.FoodAnaLayout);
            recipeBtn = itemView.findViewById(R.id.recipeBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }


    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodlistlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.ViewHolder holder, final int position) {

         CursorItem liste=list.get(position);
        holder.foodName.setText(String.valueOf(list.get(position).getName()));
       // holder.foodDate.setText(String.valueOf(list.get(position).getDate()));
        final Integer[] id = new Integer[1];

        Picasso.get().load(liste.getImage()).into(holder.foodImage);
        holder.FoodAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://10.0.2.2:5000/againlist";
                //String url = "http://localhost:5000/againlist";

                JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
                jsonobj = new JSONObject();
                try {
                    // adding some keys

                    GetSharedPreferences getSharedPreferences = new GetSharedPreferences(context);
                    String id = getSharedPreferences.getSession().getString("id",null);
                    jsonobj.put("name", String.valueOf(list.get(position).getName()));
                    jsonobj.put("user_id", id);
                    jsonobj.put("image", String.valueOf(list.get(position).getImage()));



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


                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsObjRequest2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ChangeFragments changeFragments = new ChangeFragments(context);
                        changeFragments.change(new HomeFragment());
                    }
                }, 500);





            }
        });
       holder.recipeBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               RequestQueue queue = Volley.newRequestQueue(context);
               String url = "http://10.0.2.2:5000/getid";
               //String url = "http://localhost:5000/getid";

               JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
               jsonobj = new JSONObject();
               try {
                   // adding some keys


                   jsonobj.put("name", String.valueOf(list.get(position).getName()));



               } catch (JSONException ex) {

                   ex.printStackTrace();
               }
               JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new com.android.volley.Response.Listener<JSONObject>() {

                   @Override
                   public void onResponse(JSONObject response) {

                       try{

                           JSONArray jsonArray = response.getJSONArray("cursor");




                               JSONObject Jobj = jsonArray.getJSONObject(0);
                               ChangeFragments changeFragments = new ChangeFragments(context);
                               changeFragments.changeWithParameters(new FoodDetailFragment(), String.valueOf(Jobj.getInt("id")));




















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

               RequestQueue requestQueue = Volley.newRequestQueue(context);
               requestQueue.add(jsObjRequest2);



           }
       });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue2 = Volley.newRequestQueue(context);
                String url = "http://10.0.2.2:5000/deletelist";
                //String url = "http://localhost:5000/deletelist";

                JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
                jsonobj = new JSONObject();
                try {
                    // adding some keys


                    jsonobj.put("name", String.valueOf(list.get(position).getName()));
                    jsonobj.put("rating", 0);
                    jsonobj.put("id", String.valueOf(list.get(position).getId()));
                    jsonobj.put("user_id", String.valueOf(list.get(position).getUserId()));



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

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsObjRequest2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ChangeFragments changeFragments = new ChangeFragments(context);
                        changeFragments.change(new HomeFragment());
                    }
                }, 500);


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
