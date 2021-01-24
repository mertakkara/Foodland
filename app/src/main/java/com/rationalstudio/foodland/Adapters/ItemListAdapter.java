package com.rationalstudio.foodland.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rationalstudio.foodland.Fragments.ShopFragment;
import com.rationalstudio.foodland.Models.ItemItem;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.ChangeFragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.rationalstudio.foode.Fragments.FoodDetailFragment;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    List<ItemItem> list;
    Context context;

    public ItemListAdapter(List<ItemItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Button removeButton;


        public ViewHolder( View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            removeButton = itemView.findViewById(R.id.removeButton);

          //  foodDate = itemView.findViewById(R.id.foodDate);

           // FoodAnaLayout = itemView.findViewById(R.id.FoodAnaLayout);
        }
    }


    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.basketitemlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, final int position) {

        ItemItem liste=list.get(position);
        holder.name.setText(String.valueOf(list.get(position).getItem()));
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //silme işlemi yapılacak String.valueOf(list.get(position).getId()
                String url = "http://10.0.2.2:5000/deleteitem";
               // String url = "http://localhost:5000/deleteitem";
                JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
                jsonobj = new JSONObject();
                try {
                    // adding some keys
                    jsonobj.put("id", String.valueOf(list.get(position).getId()));


                } catch (JSONException ex) {

                    ex.printStackTrace();
                }
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new com.android.volley.Response.Listener<JSONObject>() {


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
                requestQueue.add(jsObjRequest);


                ChangeFragments changeFragments = new ChangeFragments(context);
                changeFragments.change(new ShopFragment());



            }
        });
       // holder.foodDate.setText(String.valueOf(list.get(position).getDate()));






    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
