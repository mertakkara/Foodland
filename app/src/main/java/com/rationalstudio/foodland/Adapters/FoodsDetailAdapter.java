package com.rationalstudio.foodland.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rationalstudio.foodland.Fragments.FoodDetailFragment;
import com.rationalstudio.foodland.Models.RecipeDetailModel;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.Utils.ChangeFragments;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

//import com.rationalstudio.foode.Fragments.FoodDetailFragment;

public class FoodsDetailAdapter extends RecyclerView.Adapter<FoodsDetailAdapter.ViewHolder> {
    List<RecipeDetailModel> list;
    Context context;

    public FoodsDetailAdapter(List<RecipeDetailModel> list, Context context) {
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
    public FoodsDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodlistitemlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodsDetailAdapter.ViewHolder holder, final int position) {
        holder.foodName.setText(list.get(position).getName().toString());
        holder.FoodAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeFragments changeFragments = new ChangeFragments(context);
                changeFragments.changeWithParameters(new FoodDetailFragment(), String.valueOf(list.get(position).getId()));
            }
        });

       Picasso.get().load(list.get(position).getImage()).into(holder.foodImage);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
