package chenj.android.spriteweather.whole;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

import chenj.android.spriteweather.R;
import chenj.android.spriteweather.util.Utility;

/**
 * Created by Administrator on 2017/11/17 0017.
 */

public class MyApplication extends Application {
    //用于在SharedPreference文件存储天气信息的key值
    public static final String PREFERENCE_WEATHER_KEY = "weather_key";
    public static final String PREFERENCE_BING_PIC = "bing_pic";
    public static final String PREFERENCE_IS_UPDATE = "is_update";
    public static final String PREFERENCE_UPDATE_FREQUENCY = "update_frequency";

    public static final String ACTION_UPDATE_UI = "ACTION_UPDATE_UI";

    private static String weather_request_key;
    private static String area_request_url;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        weather_request_key = context.getString(R.string.weather_request_key);
        area_request_url = context.getString(R.string.area_request_url);

        //初次装在应用时，初始化参数
        MySharedPreference.putValue(PREFERENCE_IS_UPDATE, true);
        MySharedPreference.putValue(PREFERENCE_UPDATE_FREQUENCY, "1");
    }

    public static Context getContext(){
        return context;
    }

    public static String getWeatherRequestKey(){
        return weather_request_key;
    }

    public static String getAreaRequestUrl(){
        return area_request_url;
    }
}
