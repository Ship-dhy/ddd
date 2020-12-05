package chenj.android.spriteweather.whole;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import chenj.android.spriteweather.WeatherActivity;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class MySharedPreference {

    private static SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    private static SharedPreferences.Editor editor = sharedPreferences.edit();


    public static SharedPreferences getSharedPreference(){
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getEditor(){
        return editor;
    }

    public static String getValue(String key,String defaultValue){
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void putValue(String key, String value){
        editor.putString(key,value);
        editor.apply();
    }

    public static boolean getValue(String key, boolean defaultValue){
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void putValue(String key, boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }


}
