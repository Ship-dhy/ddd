package chenj.android.spriteweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import chenj.android.spriteweather.whole.MyApplication;

/**
 * 需增加的内容：
 * 1.提供多套图片根据天气的不同来设置背景
 * 2.提供多个城市的选择
 * 3.显示更加完整的信息
 * 4.设置功能已经增加，目前增加了是否自动更新，更新频率的设置
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String weatherString = sharedPreferences.getString(MyApplication.PREFERENCE_WEATHER_KEY, null);
        if(weatherString != null) {
            WeatherActivity.startAction(MainActivity.this);
            finish();
        }
    }
}
