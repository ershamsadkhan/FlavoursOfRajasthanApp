package com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SAM on 6/6/2017.
 */

public class TextStorage {

    public Context context;
    public String MY_PREF="My_Pref";
    public TextStorage(Context context){
        this.context=context;
    }
    public void storeUserId(String Value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserId", Value);
        editor.apply();
    }

    public String getUserId(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        return sharedPreferences.getString("UserId","0");
    }

    public void storeUserName(String Value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", Value);
        editor.apply();
    }

    public String getUserName(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        return sharedPreferences.getString("UserName","");
    }

    public void storeUserPwd(String Value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserPwd", Value);
        editor.apply();
    }

    public String getUserPwd(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        return sharedPreferences.getString("UserPwd","");
    }

    public void storeCartData(String Value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Cart", Value);
        editor.apply();
    }

    public String getCartData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        return sharedPreferences.getString("Cart","");
    }
}
