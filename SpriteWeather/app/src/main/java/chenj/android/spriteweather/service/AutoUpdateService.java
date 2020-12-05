package chenj.android.spriteweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import chenj.android.spriteweather.WeatherActivity;
import chenj.android.spriteweather.gson.Weather;
import chenj.android.spriteweather.util.HttpUtil;
import chenj.android.spriteweather.util.Utility;
import chenj.android.spriteweather.whole.MyApplication;
import chenj.android.spriteweather.whole.MyLocalBroadcastManager;
import chenj.android.spriteweather.whole.MySharedPreference;

/**
 * 自动更新日期和背景图
 */
public class AutoUpdateService extends Service {
    public static void startService(Context context){
        Intent intent = new Intent(context, AutoUpdateService.class);
        context.startService(intent);
    }


    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        updateBingPic();
       // updateWeather();

        //发送更新UI的广播
        MyLocalBroadcastManager.getLocalBroadcastManager().sendBroadcast(new Intent(MyApplication.ACTION_UPDATE_UI));

        //只有打开自动更新是才会自动更新
        if(MySharedPreference.getValue(MyApplication.PREFERENCE_IS_UPDATE, true)) {
            //获取自动更新的时间
            int hour = Integer.parseInt(MySharedPreference.getValue(MyApplication.PREFERENCE_UPDATE_FREQUENCY, "1"));
            Log.d("hour", hour+"");
            //启动定时器
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent i = new Intent(this, AutoUpdateService.class);
            PendingIntent pi = PendingIntent.getService(AutoUpdateService.this, 0, i, 0);
            //一小时更新一次
            long time = SystemClock.elapsedRealtime() + hour * 60 * 60 * 1000;
            manager.cancel(pi);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, pi);
        }
        return super.onStartCommand(intent,flags,startId);
    }

    private void updateWeather(){
        String weatherString = MySharedPreference.getValue(MyApplication.PREFERENCE_WEATHER_KEY, null);
        if(weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weather_id = weather.basic.weatherId;
            HttpUtil.sendOkHttpRequest(Utility.getWeatherRequestUrl(weather_id), new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Utility.showToast("获取天气信息失败");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String weatherString = response.body().string();
                    final Weather weather = Utility.handleWeatherResponse(weatherString);
                    //存储天气信息到Preference文件
                    if (weather != null && weather.status.equalsIgnoreCase("ok")) {
                        MySharedPreference.putValue(MyApplication.PREFERENCE_WEATHER_KEY, weatherString);
                    } else {
                        Utility.showToast("获取天气信息失败");
                    }
                }
            });
        }
    }

    private void updateBingPic(){
        HttpUtil.sendOkHttpRequest(Utility.getBingPicUri(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String bingPicUri = response.body().string()+"111111";
                MySharedPreference.putValue(MyApplication.PREFERENCE_BING_PIC, bingPicUri);
                Log.d("AutoUpdateService", bingPicUri);
            }
        });
    }
}
