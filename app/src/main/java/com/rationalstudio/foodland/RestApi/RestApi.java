package com.rationalstudio.foodland.RestApi;


import com.rationalstudio.foodland.Models.FoodModel;
import com.rationalstudio.foodland.Models.RecipeDetailModel;
import com.rationalstudio.foodland.Models.RecipeModel;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestApi {
    @FormUrlEncoded
    @POST("api/register")
    Call<RecipeModel> registerUser(@Field("emailadress") String emailadress, @Field("username") String username, @Field("password") String password, @Field("firstname") String firstname, @Field("lastname") String lastname);

    @GET("/food")
    Call<List<RecipeDetailModel>> getFoods();




    @FormUrlEncoded
    @POST("/fooddetail")
    Call<List<RecipeDetailModel>> getFood(@Field("id") JSONObject id);


    @FormUrlEncoded
    @POST("/foodlist")
    Call<List<FoodModel>> getFoodsList(@Field("id") JSONObject id);
}
