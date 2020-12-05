package chenj.android.spriteweather.selectarea;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chenj.android.spriteweather.MainActivity;
import chenj.android.spriteweather.R;
import chenj.android.spriteweather.SettingActivity;
import chenj.android.spriteweather.WeatherActivity;
import chenj.android.spriteweather.db.City;
import chenj.android.spriteweather.db.County;
import chenj.android.spriteweather.db.Province;
import chenj.android.spriteweather.gson.Weather;
import chenj.android.spriteweather.util.HttpUtil;
import chenj.android.spriteweather.util.UiUtilty;
import chenj.android.spriteweather.util.Utility;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class SelectAreaFragment extends Fragment {
    private static final int LEVEL_PROVINCE = 1;//省
    private static final int LEVEL_CITY = 2;//市
    private static final int LEVEL_COUNTY = 3;//区
    private int current_level;//当前地区级别

    private List<Province> provinceList;//省份列表
    private List<City> cityList;//城市列表
    private List<County> countyList;//县列表

    private Province selectedProvince;//选中的省
    private City selectedCity;//选中的市
    private County selectedCounty;//选中的县


    private List<String> datalist = new ArrayList<>();//存放名称

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;//地区显示列表
    private Button back_btn;//返回按键
    private TextView title_tv;//显示名称
    private Button setting;//设置

    private AreaRecyclerViewAdapter adapter;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        RelativeLayout head_layout = (RelativeLayout)view.findViewById(R.id.head_layout);
        recyclerView = (RecyclerView)view.findViewById(R.id.area_choose_list);
        back_btn = (Button)view.findViewById(R.id.area_bcak_btn);
        title_tv = (TextView)view.findViewById(R.id.area_title_text);
        setting = (Button)view.findViewById(R.id.setting);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(manager);


        adapter = new AreaRecyclerViewAdapter(datalist, new RecyclerViewListener() {
            @Override
            public void onClickItem(int pos) {
                //选中某个元素
                if(current_level == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(pos);
                    queryCities();
                }else if(current_level == LEVEL_CITY){
                    selectedCity = cityList.get(pos);
                    queryCounties();
                }else {
                    selectedCounty = countyList.get(pos);

                    if(getActivity() instanceof MainActivity){
                        WeatherActivity.startAction(getActivity(), selectedCounty.getWeatherId());
                        getActivity().finish();
                    } else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity)getActivity();
                        activity.drawlayout.closeDrawer(Gravity.START|Gravity.TOP);
                        activity.weather_swip_refresh_layout.setRefreshing(true);
                        activity.requestWeather(selectedCounty.getWeatherId());
                    }

                }
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_level == LEVEL_COUNTY){
                    queryCities();
                }else if(current_level == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof MainActivity){
                    SettingActivity.startAction(getActivity());
                } else if(getActivity() instanceof WeatherActivity){
                    WeatherActivity activity = (WeatherActivity)getActivity();
                    activity.drawlayout.closeDrawer(Gravity.START|Gravity.TOP);
                    SettingActivity.startAction(getActivity());
                }
            }
        });

        queryProvinces();
    }


    /**
     * 显示加载进度对话框
     */
    private void showProcessDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("数据加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeProcessDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    //查询省的数据
    private void queryProvinces(){
        title_tv.setText("中国");
        back_btn.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size() > 0){
            datalist.clear();
            //若数据库中有值，则直接从数据库中取数据
            for(Province province : provinceList){
                datalist.add(province.getName());
            }
            adapter.notifyDataSetChanged();
            current_level = LEVEL_PROVINCE;
        }else{
            //否则从网络请求数据
            queryFromServer(Utility.getAreaRequestUrl(), "province");
        }
    }

    private void queryCities(){
        title_tv.setText(selectedProvince.getName());
        back_btn.setVisibility(View.VISIBLE);
        //查找省下面对应的市
        cityList = DataSupport.where("provinceId=?",String.valueOf(selectedProvince.getProvinceId())).find(City.class);

        if(cityList.size() > 0){
            datalist.clear();
            //若数据库中有值，则直接从数据库中取数据
            for(City city : cityList){
                datalist.add(city.getName());
            }
            adapter.notifyDataSetChanged();
            current_level = LEVEL_CITY;
        }else{
            //否则从网络请求数据
            queryFromServer(Utility.getAreaRequestUrl(selectedProvince.getProvinceId()), "city");
        }
    }



    private void queryCounties(){
        title_tv.setText(selectedCity.getName());
        back_btn.setVisibility(View.VISIBLE);
        //查找省下面对应的市
        countyList = DataSupport.where("cityId=?", String.valueOf(selectedCity.getCityId())).find(County.class);

        if(countyList.size() > 0){
            datalist.clear();
            //若数据库中有值，则直接从数据库中取数据
            for(County county : countyList){
                datalist.add(county.getName());
            }
            adapter.notifyDataSetChanged();
            current_level = LEVEL_COUNTY;
        }else{
            //否则从网络请求数据
            queryFromServer(Utility.getAreaRequestUrl(selectedProvince.getProvinceId(),selectedCity.getCityId()), "county");
        }
    }

    private void queryFromServer(String address, final String type){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //回到主线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utility.showToast("加载失败!");
                        closeProcessDialog();
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {
                boolean result = false;
                String reponseText = response.body().string();
                if(type.equals("province")){
                    result = Utility.handleProvinceResponse(reponseText);
                }
                if(type.equals("city")){
                    result = Utility.handleCityResponse(reponseText, selectedProvince.getProvinceId());
                }
                if(type.equals("county")){
                    result = Utility.handleCountyResponse(reponseText, selectedCity.getCityId());
                }

                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(type.equals("province")){
                                queryProvinces();
                            }
                            if(type.equals("city")){
                                queryCities();
                            }
                            if(type.equals("county")){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }




}
