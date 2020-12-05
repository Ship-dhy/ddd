package chenj.android.spriteweather.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chenj.android.spriteweather.R;
import chenj.android.spriteweather.db.City;
import chenj.android.spriteweather.db.County;
import chenj.android.spriteweather.db.Province;
import chenj.android.spriteweather.gson.Weather;
import chenj.android.spriteweather.whole.MyApplication;


/**
 * 工具类
 * Created by Administrator on 2017/11/16 0016.
 */

public class Utility {

    /**
     * 解析服务器返回的json格式的省字符串，并保存到数据库
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Province province = new Province();
                province.setName(object.getString("name"));
                province.setProvinceId(object.getInt("id"));
                province.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 解析服务器返回的json格式的市字符串，并保存到数据库
     * @param response
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId){
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                City city = new City();
                city.setName(object.getString("name"));
                city.setCityId(object.getInt("id"));
                city.setProvinceId(provinceId);
                city.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析服务器返回的json格式的县字符串，并保存到数据库
     * @param response
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityId){
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                County county = new County();
                county.setName(object.getString("name"));
                county.setWeatherId(object.getString("weather_id"));
                county.setCityId(cityId);
                county.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject heWeather = new JSONObject(response);
            JSONArray jsonArray = heWeather.getJSONArray("HeWeather");

            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException", e.getMessage());
        }
        return null;
    }

    public static void showToast(String msg){
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取天气的url，传入weatherId
     * @return
     */
    public static String getWeatherRequestUrl(String weatherId){
        return "http://guolin.tech/api/weather?key=" + MyApplication.getWeatherRequestKey() + "&cityid=" + weatherId;
    }

    //网络获取省级地址
    public static String getAreaRequestUrl(){
        return MyApplication.getAreaRequestUrl();
    }

    //网络获取市级地址
    public static String getAreaRequestUrl(int province){
        return getAreaRequestUrl()+province+"/";
    }

    //网络获取县级地址
    public static String getAreaRequestUrl(int province, int cityId){
        return getAreaRequestUrl(province)+cityId;
    }


    //从服务器获取必应图片地址
    public static String getBingPicUri(){
        return "http://guolin.tech/api/bing_pic";
    }

    public static  String getRandom(){
        Random random = new Random();
        return "" + random.nextInt(20);
    }

    /**
     * 是否是白天
     * @return
     */
    public static boolean isDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour= sdf.format(new Date());
	    int k = Integer.parseInt(hour);
	    Log.d("hour", "hour:"+k);
        if ((k>=0 && k<6) ||(k >=18 && k<24)){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据《环境空气质量指数（AQI）技术规定（试行）》（HJ 633—2012）规定：空气污染指数划分为0－50、51－100、101－150、151－200、201－300和大于300六档，对应于空气质量的六个级别，指数越大，级别越高，说明污染越严重，对人体健康的影响也越明显。
     空气污染指数为0－50，空气质量级别为一级，空气质量状况属于优。此时，空气质量令人满意，基本无空气污染，各类人群可正常活动。
     空气污染指数为51－100，空气质量级别为二级，空气质量状况属于良。此时空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响，建议极少数异常敏感人群应减少户外活动。[3]
     空气污染指数为101－150，空气质量级别为三级，空气质量状况属于轻度污染。此时，易感人群症状有轻度加剧，健康人群出现刺激症状。建议儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼。[3]
     空气污染指数为151－200，空气质量级别为四级，空气质量状况属于中度污染。此时，进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响，建议疾病患者避免长时间、高强度的户外锻练，一般人群适量减少户外运动。[3]
     空气污染指数为201－300，空气质量级别为五级，空气质量状况属于重度污染。此时，心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状，建议儿童、老年人和心脏病、肺病患者应停留在室内，停止户外运动，一般人群减少户外运动。[3]
     空气污染指数大于300，空气质量级别为六级，空气质量状况属于严重污染。此时，健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病，建议儿童、老年人和病人应当留在室内，避免体力消耗，一般人群应避免户外活动。
     */
    public static int getAirQualityLevel(String aqiStr){
        int level = -1;
        int api = Integer.parseInt(aqiStr);
        if(api< 50){
            level = 1;
        }else if(api < 100){
            level = 2;
        }else if(api < 150){
            level = 3;
        }else if(api < 200){
            level = 4;
        }else if(api < 300){
            level = 5;
        }else{
            level = 6;
        }
        return level;
    }

    private static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据code找到对应天气的图标ID
     * @param codeName
     * @return
     */
    public static int getDrawableResId(String codeName){
        int id = 0;
        String[] weather_codes = MyApplication.getContext().getResources().getStringArray(R.array.weather_codes);
        for(String str:weather_codes) {
            String[] code = str.split("-");
            if(code[0].equals(codeName)){
                //找到代码，获取相应的ID资源
                id = getResId(code[1], R.drawable.class);
                Log.d("CODE", "CODE:"+code[0]+ "--"+code[1]);
                break;
            }
        }

        return id;
    }

    /**
     * 更具code获取动态的类型
     * 0-无
     * 1-云
     * 2-小雨
     * 3-中雨
     * 4-大雨
     * 5-小雪
     * 6-中雪
     * 7-大雪
     * 8-雨加雪
     * 9-雾霾，沙城暴
     * @param codeName
     * @return
     */
    public static int getWeatherAnmiType(String codeName){
        int type = 0;


        String[] weather_codes = MyApplication.getContext().getResources().getStringArray(R.array.weather_codes);
        for(String str:weather_codes) {
            String[] code = str.split("-");
            if(code[0].equals(codeName)){
                type = Integer.parseInt(code[2]);
                break;
            }
        }

        return type;
    }

}
