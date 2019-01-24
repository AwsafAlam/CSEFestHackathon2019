package io.github.utshaw.myhealth.util;

import android.content.SharedPreferences;



public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(String token){
        editor.putString("ACCESS_TOKEN", token).commit();
    }

    public String getToken(){
        return prefs.getString("ACCESS_TOKEN", null);
    }

    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
    }



}
