package com.rationalstudio.foodland.Adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Fragments.HomeFragment;
import com.rationalstudio.foodland.Models.RecipeModel;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

//import com.rationalstudio.foode.Fragments.FoodDetailFragment;

public class FoodListAdapter2 extends RecyclerView.Adapter<FoodListAdapter2.ViewHolder> {
    List<RecipeModel> list;
    Context context;


    public FoodListAdapter2(List<RecipeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName,foodDate;
        CircleImageView foodImage;
        LinearLayout FoodAnaLayout;

        public ViewHolder( View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
          //  foodDate = itemView.findViewById(R.id.foodDate);
            foodImage = itemView.findViewById(R.id.foodImage);
            FoodAnaLayout = itemView.findViewById(R.id.FoodAnaLayout);

        }
    }


    @NonNull
    @Override
    public FoodListAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodlistlayout2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter2.ViewHolder holder, final int position) {

        RecipeModel liste=list.get(position);
        holder.foodName.setText(String.valueOf(list.get(position).getName()));
       // holder.foodDate.setText(String.valueOf(list.get(position).getDate()));


       Picasso.get().load(liste.getImage()).into(holder.foodImage);
       holder.FoodAnaLayout.setOnClickListener(new View.OnClickListener() {
               @Override
           public void onClick(View view) {
               RequestQueue queue = Volley.newRequestQueue(context);
               String url = "http://10.0.2.2:5000/list";
               //String url = "http://localhost:5000/list";

               JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
               jsonobj = new JSONObject();
               try {
                   // adding some keys

                   GetSharedPreferences getSharedPreferences = new GetSharedPreferences(context);
                   String id = getSharedPreferences.getSession().getString("id",null);
                   jsonobj.put("name", String.valueOf(list.get(position).getName()));
                   jsonobj.put("user_id", id);
                   jsonobj.put("id", String.valueOf(list.get(position).getId()));
                   jsonobj.put("image", String.valueOf(list.get(position).getImage()));
                   jsonobj.put("rating", 5.0);


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
