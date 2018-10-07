package com.testbook.arshad.testbookchatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by arshad on 05/10/18.
 */

public class PreferenceManager {

    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IMAGE = "image";

    private static PreferenceManager ourInstance;
    private SharedPreferences sharedPreferences;
    private Context context;

    private PreferenceManager(Context context) {
        this.sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        /*this.gson = new Gson();/*new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();*/
    }

    public static PreferenceManager getInstance(Context context) {
        if (ourInstance == null){
            ourInstance = new PreferenceManager(context);
        }
        ourInstance.context = context;
        return ourInstance;
    }

    public void saveFirstName(String firstName) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.commit();
    }

    public String getFirstName() {
        return  this.sharedPreferences.getString(KEY_FIRST_NAME, "");
    }

    public void saveLastName(String lastName) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(KEY_LAST_NAME, lastName);
        editor.commit();
    }

    public String getLastName() {
        return  this.sharedPreferences.getString(KEY_LAST_NAME, "");
    }

    public void saveUsername(String username) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername() {
        return  this.sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void saveImage(String image) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(KEY_IMAGE, image);
        editor.commit();
    }

    public String getImage() {
        return  this.sharedPreferences.getString(KEY_IMAGE, "");
    }

}

