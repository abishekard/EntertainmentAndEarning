package com.example.browser;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

public class info {
    private String points;
    private String userName;
    public static String nVersion;
    private String email;
    private SharedPreferences sharedPreferences;

    public info(Context context){
        sharedPreferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);

    }
    public static int getRandomNumber(int min,int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    public String getPoints() {
        points=sharedPreferences.getString("points","");
        return points;
    }

    public String getUserName() {
        userName=sharedPreferences.getString("name","");
        return userName;
    }



    public String getEmail() {
        email=sharedPreferences.getString("email","");
        return email;
    }



    public void setPoints(String points) {
        this.points = points;
        sharedPreferences.edit().putString("points",points).apply();
    }

    public void setUserName(String userName) {
        this.userName = userName;
        sharedPreferences.edit().putString("name",userName).apply();
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email",email).apply();
    }



  public void remove(){
        sharedPreferences.edit().clear().apply();

  }

}
