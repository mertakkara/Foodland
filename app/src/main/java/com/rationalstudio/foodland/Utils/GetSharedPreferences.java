package com.rationalstudio.foodland.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GetSharedPreferences {
    private SharedPreferences sharedPreferences;
    private Context context;
    public GetSharedPreferences(Context context){
        this.context = context;

    }

    public  SharedPreferences getSession(){
        sharedPreferences = context.getApplicationContext().getSharedPreferences("session",0);
        return sharedPreferences;
    }
    public void setSession(String username,String password,String id ){
        sharedPreferences = context.getApplicationContext().getSharedPreferences("session",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("id",id);
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("id",id);
        editor.commit();
    }
    public void deleteToSession(){
        sharedPreferences = context.getApplicationContext().getSharedPreferences("session",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
