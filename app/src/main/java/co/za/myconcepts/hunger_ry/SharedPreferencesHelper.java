package co.za.myconcepts.hunger_ry;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static SharedPreferences sharedPreferences;

    public static void setStringChoice(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringPreference(String key, Context context){
        sharedPreferences = context.getSharedPreferences(Constants.PACKAGE, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void setBooleanChoice(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static Boolean getBooleanPreference(String key, Context context){
        sharedPreferences = context.getSharedPreferences(Constants.PACKAGE, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
}















