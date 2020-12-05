package chenj.android.spriteweather;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import chenj.android.spriteweather.broadcast.UpdateUiReceiver;
import chenj.android.spriteweather.compent.animview.CloundView;
import chenj.android.spriteweather.compent.animview.RainView;
import chenj.android.spriteweather.compent.animview.SnowView;
import chenj.android.spriteweather.gson.ForeCast;
import chenj.android.spriteweather.gson.Weather;
import chenj.android.spriteweather.service.AutoUpdateService;
import chenj.android.spriteweather.util.HttpUtil;
import chenj.android.spriteweather.util.UiUtilty;
import chenj.android.spriteweather.util.Utility;
import chenj.android.spriteweather.whole.MyApplication;
import chenj.android.spriteweather.whole.MyLocalBroadcastManager;
import chenj.android.spriteweather.whole.MySharedPreference;

public class WeatherActivity extends AppCompatActivity {
    //用于activity间传递数据的名称
    private static final String WEATHER_ID = "weather_id";

    //控件
    private FrameLayout weather_main_layout;
    private ScrollView weather_layout;
    private TextView title_city;
    private TextView title_update_time;
    private TextView degree_text;
    private ImageView now_icon;
    private TextView now_info;
    private TextView wind_tv;
    private LinearLayout forecast_layout;
    private TextView air_quality;
    private TextView aqi_tv;
    private TextView pm25_tv;
    private TextView wash_car_tv;
    private TextView sport_tv;
    private TextView dress_tv;
    private TextView tourism_tv;
    private TextView ultraviolet;
    public SwipeRefreshLayout weather_swip_refresh_layout;
    private Button home_up;
    public DrawerLayout drawlayout;
    //用于存放天气信息查询用到 的ID，提供给下拉刷新使用
    private String swiper_refresh_weather_id;

    //动画控件
    private CloundView clound_anmi;
    private RainView   light_rain_anmi;
    private RainView   rain_anmi;
    private RainView   heavy_rain_anmi;
    private SnowView   light_snow_anmi;
    private SnowView   snow_anmi;
    private SnowView   heavy_snow_anmi;



    //定义接受接受服务更新UI的广播接收器
    private UpdateUiReceiver updateUiReceiver = new UpdateUiReceiver(new UpdateUiReceiver.UiDataChangedListener() {
        @Override
        public void onUiChanged() {
            Log.d("WeatherActivity","更新页面元素");
            showMain();
        }
    });

    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将状态栏背景图片与我们的app一致, 此功能在5.0系统后才支持
        UiUtilty.setStatusBackgroundWithActivity(this);

        setContentView(R.layout.activity_weather);
        //初始化各种控件
        initViews();

        //控件的事件监听处理
        viewListenerProcess();

        //绑定服务
        AutoUpdateService.startService(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册广播接受
        intentFilter = new IntentFilter();
        intentFilter.addAction(MyApplication.ACTION_UPDATE_UI);
        MyLocalBroadcastManager.getLocalBroadcastManager().registerReceiver(updateUiReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注销广播接受
        MyLocalBroadcastManager.getLocalBroadcastManager().unregisterReceiver(updateUiReceiver);
    }

    //-------------------------------------供外部调用方法----------------------------------------------------------
    public  static void startAction(Context context, String weatherId){
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(WEATHER_ID, weatherId);
        context.startActivity(intent);
    }


    public  static void startAction(Context context){
        Intent intent = new Intent(context, WeatherActivity.class);
        context.startActivity(intent);
    }




    //UI显示- 所有的显示流程
    private void showMain(){
        String weatherString = MySharedPreference.getValue(MyApplication.PREFERENCE_WEATHER_KEY, null);

        if(weatherString != null){
            Log.d("WeatherActivity", "weatherString:"+ weatherString);
            Weather weather = Utility.handleWeatherResponse(weatherString);
            swiper_refresh_weather_id = weather.basic.weatherId;
            //显示信息
            showWeatherInfo(weather);
        } else {
            String weatherId = getIntent().getStringExtra(WEATHER_ID);

            swiper_refresh_weather_id = weatherId;
            //先设置天气显示视图不可见
            weather_layout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String bingPic = MySharedPreference.getValue(MyApplication.PREFERENCE_BING_PIC, "null");

//        Log.d("WeatherActivity", "bingPic:"+ bingPic);
//        if(bingPic != null){
//            Glide.with(this).load(bingPic).into(bing_pic_iv);
//        } else {
//            loadBingPic();
//        }
    }

    private void viewListenerProcess(){
        //下拉刷新时间处理
        weather_swip_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(swiper_refresh_weather_id);
            }
        });


        home_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawlayout.openDrawer(Gravity.START|Gravity.TOP);
            }
        });
    }

    /**
     * 根据类型设置动画效果
     * @param type
     */
    private void processAnmi(int type){
        //先让所有的动画不可见
        clound_anmi.setVisibility(View.GONE);
        light_rain_anmi.setVisibility(View.GONE);
        rain_anmi.setVisibility(View.GONE);
        heavy_rain_anmi.setVisibility(View.GONE);
        light_snow_anmi.setVisibility(View.GONE);
        snow_anmi.setVisibility(View.GONE);
        heavy_snow_anmi.setVisibility(View.GONE);

        if(type == 1){
            //添加云飘动的效果
            clound_anmi.setVisibility(View.VISIBLE);
        }else if(type == 2){
            //小雨
            light_rain_anmi.setVisibility(View.VISIBLE);
        }else if(type == 3){
            //中雨
            rain_anmi.setVisibility(View.VISIBLE);
            weather_main_layout.setBackgroundResource(Utility.isDay() ? R.drawable.smog_day_background:R.drawable.night_background);
        }else if(type == 4){
            //大雨
            heavy_rain_anmi.setVisibility(View.VISIBLE);
            weather_main_layout.setBackgroundResource(Utility.isDay() ? R.drawable.smog_day_background:R.drawable.night_background);
        }else if(type == 5){
            //小雪
            light_snow_anmi.setVisibility(View.VISIBLE);
        }else if(type == 6){
            //中雪
            snow_anmi.setVisibility(View.VISIBLE);
        }else if(type == 7){
            //大雪
            heavy_snow_anmi.setVisibility(View.VISIBLE);
        }else if(type == 8){
            //雨夹雪
            rain_anmi.setVisibility(View.VISIBLE);
            snow_anmi.setVisibility(View.VISIBLE);
        } else if(type == 9){
            //如果是雾天或者沙城暴天气需要换一个背景
            weather_main_layout.setBackgroundResource(Utility.isDay() ? R.drawable.smog_day_background:R.drawable.night_background);
        } else {
            weather_main_layout.setBackgroundResource(Utility.isDay() ? R.drawable.sunny_day_background:R.drawable.night_background);
        }
    }

    private void showWeatherInfo(Weather weather){
        weather_main_layout.setBackgroundResource(Utility.isDay() ? R.drawable.sunny_day_background:R.drawable.night_background);
        processAnmi(Utility.getWeatherAnmiType(weather.now.more.code));

        title_city.setText(weather.basic.citiName);
        String updatetime = weather.basic.update.loc.split(" ")[1];
        title_update_time.setText("最近更新:\n  " + updatetime);
        degree_text.setText(weather.now.temperature+"℃");
        now_info.setText(weather.now.more.info);
        //更具天气代码获取天气图标
        now_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), Utility.getDrawableResId(weather.now.more.code)));
        wind_tv.setText(weather.now.wind.dir + ":" + weather.now.wind.sc);

        forecast_layout = (LinearLayout)findViewById(R.id.forecast_layout);
        forecast_layout.removeAllViews();
        for (ForeCast foreCast : weather.foreCastList){
            int info_img_id = 0;
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, forecast_layout, false);
            TextView date_tv = (TextView)view.findViewById(R.id.date_tv);
            ImageView info_img = (ImageView)view.findViewById(R.id.info_img);
            TextView tmp = (TextView)view.findViewById(R.id.tmp);
            TextView wind = (TextView)view.findViewById(R.id.wind);

            date_tv.setText(foreCast.date.substring(5));
            info_img.setImageBitmap(BitmapFactory.decodeResource(getResources(),Utility.getDrawableResId(foreCast.more.code_d)));
            tmp.setText(foreCast.temperature.min+"℃-"+ foreCast.temperature.max+"℃");
            wind.setText(foreCast.wind.dir+":"+foreCast.wind.sc);
            forecast_layout.addView(view);
        }

        if(Utility.getAirQualityLevel(weather.aqi.city.aqi) > 2){
            aqi_tv.setTextColor(0xfff7d047);
            pm25_tv.setTextColor(0xfff7d047);
        }
        air_quality.setText("空气质量-"+ weather.aqi.city.qlty);
        aqi_tv.setText(weather.aqi.city.aqi);
        pm25_tv.setText(weather.aqi.city.pm25);
        wash_car_tv.setText("洗车指数:" + weather.suggestion.washCar.info);
        sport_tv.setText("运动指数:" + weather.suggestion.sport.info);
        dress_tv.setText("穿衣指数:" + weather.suggestion.dress.info);
        tourism_tv.setText("旅游指数:" + weather.suggestion.tourism.info);
        ultraviolet.setText("紫外线指数:" + weather.suggestion.ultraviolet.info);
        weather_layout.setVisibility(View.VISIBLE);
    }

    public void requestWeather(String weatherId){
        swiper_refresh_weather_id = weatherId;
        //String responseContent = getContext().getString(R.string.testWeatherData);
        HttpUtil.sendOkHttpRequest(Utility.getWeatherRequestUrl(weatherId), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utility.showToast("获取天气信息失败");
                    }
                });
                //关闭刷新状态提示
                weather_swip_refresh_layout.setRefreshing(false);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String weatherString = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(weatherString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //存储天气信息到Preference文件
                        if(weather!=null && weather.status.equalsIgnoreCase("ok")){
                            MySharedPreference.putValue(MyApplication.PREFERENCE_WEATHER_KEY, weatherString);
                            //显示天气信息
                            showWeatherInfo(weather);
                        }else {
                            Utility.showToast("获取天气信息失败");
                        }

                        //关闭刷新状态提示
                        weather_swip_refresh_layout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void initViews(){
        weather_main_layout = (FrameLayout)findViewById(R.id.weather_main_layout);
        weather_layout = (ScrollView)findViewById(R.id.weather_layout);
        title_city = (TextView)findViewById(R.id.title_city);
        title_update_time = (TextView)findViewById(R.id.title_update_time);
        degree_text = (TextView)findViewById(R.id.degree_text);
        now_icon = (ImageView) findViewById(R.id.now_icon);
        now_info = (TextView)findViewById(R.id.now_info);
        wind_tv = (TextView)findViewById(R.id.wind_tv);
        forecast_layout = (LinearLayout)findViewById(R.id.forecast_layout);
        air_quality = (TextView)findViewById(R.id.air_quality);
        aqi_tv = (TextView)findViewById(R.id.aqi_tv);
        pm25_tv = (TextView)findViewById(R.id.pm25_tv);
        wash_car_tv = (TextView)findViewById(R.id.wash_car_tv);
        sport_tv = (TextView)findViewById(R.id.sport_tv);
        dress_tv = (TextView)findViewById(R.id.dress_tv);
        tourism_tv = (TextView)findViewById(R.id.tourism_tv);
        ultraviolet = (TextView)findViewById(R.id.ultraviolet);
        //bing_pic_iv = (ImageView)findViewById(R.id.bing_pic_iv);
        weather_swip_refresh_layout = (SwipeRefreshLayout)findViewById(R.id.weather_swip_refresh_layout);
        home_up = (Button)findViewById(R.id.home_up);
        drawlayout = (DrawerLayout)findViewById(R.id.drawlayout);

        clound_anmi = (CloundView)findViewById(R.id.clound_anmi);
        light_rain_anmi = (RainView) findViewById(R.id.light_rain_anmi);
        rain_anmi = (RainView) findViewById(R.id.rain_anmi);
        heavy_rain_anmi = (RainView) findViewById(R.id.heavy_rain_anmi);
        light_snow_anmi = (SnowView) findViewById(R.id.light_snow_anmi);
        snow_anmi = (SnowView) findViewById(R.id.snow_anmi);
        heavy_snow_anmi = (SnowView) findViewById(R.id.heavy_snow_anmi);
    }


}
