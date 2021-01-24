package com.rationalstudio.foodland.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Models.RecipeDetailModel;
import com.rationalstudio.foodland.Models.RecipeModel;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

//import com.rationalstudio.foode.Fragments.FoodDetailFragment;

public class Foods2Adapter extends RecyclerView.Adapter<Foods2Adapter.ViewHolder> {
    List<RecipeModel> list;
    Context context;



    public Foods2Adapter(List<RecipeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName;
        CircleImageView foodImage;
        LinearLayout FoodAnaLayout;

        public ViewHolder( View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodImage = itemView.findViewById(R.id.foodImage);
            FoodAnaLayout = itemView.findViewById(R.id.FoodAnaLayout);
        }
    }


    @NonNull
    @Override
    public Foods2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodlistitemlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Foods2Adapter.ViewHolder holder, final int position) {
        holder.foodName.setText(list.get(position).getName().toString());
        holder.FoodAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, " Yemek sectiniz.",Toast.LENGTH_SHORT).show();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://10.0.2.2:5000/rating";
                //String url = "http://localhost:5000/rating";

                JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
                jsonobj = new JSONObject();
                try {
                    // adding some keys

                    GetSharedPreferences getSharedPreferences = new GetSharedPreferences(context);
                    String id = getSharedPreferences.getSession().getString("id",null);

                    jsonobj.put("user", id);
                    jsonobj.put("recipe", String.valueOf(list.get(position).getId()));
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
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsObjRequest2);





                //Intent intent = new Intent(context, Setup2Activity.class);
                //intent.putExtra("id",String.valueOf(list.get(position).getId()));
                //context.startActivity(intent);

               // ChangeFragments changeFragments = new ChangeFragments(context);
                //changeFragments.changeWithParameters(new FoodDetailFragment(), String.valueOf(list.get(position).getId()));
            }
        });

       Picasso.get().load(list.get(position).getImage()).into(holder.foodImage);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
