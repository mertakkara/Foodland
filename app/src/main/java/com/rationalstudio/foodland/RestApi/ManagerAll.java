package com.rationalstudio.foodland.RestApi;



import com.rationalstudio.foodland.Models.FoodModel;
import com.rationalstudio.foodland.Models.RecipeDetailModel;
import com.rationalstudio.foodland.Models.RecipeModel;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;

public class ManagerAll extends BaseManager {
    private  static ManagerAll ourInstance = new ManagerAll();

    public  static synchronized ManagerAll getInstance()
    {
        return  ourInstance;
    }

    public Call<RecipeModel> kayitol(String emailadress , String username, String password,String firstname,String lastname)
    {
        Call<RecipeModel> x = getRestApi().registerUser(emailadress,username,password,firstname,lastname);
        return  x ;
    }
    public Call<List<RecipeDetailModel>> getFoods()
    {
        Call<List<RecipeDetailModel>> x = getRestApi().getFoods();
        return  x ;
    }



    public Call<List<RecipeDetailModel>> getFood(JSONObject id)
    {
        Call<List<RecipeDetailModel>> x = getRestApi().getFood(id);
        return  x ;
    }

    public Call<List<FoodModel>> getFoodsList(JSONObject id)
    {
        Call<List<FoodModel>> x = getRestApi().getFoodsList(id);
        return  x ;
    }

}
