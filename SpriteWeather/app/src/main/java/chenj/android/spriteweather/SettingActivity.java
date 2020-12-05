package chenj.android.spriteweather;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chenj.android.spriteweather.compent.PickerDialog;
import chenj.android.spriteweather.compent.ToggleView;
import chenj.android.spriteweather.service.AutoUpdateService;
import chenj.android.spriteweather.whole.MyApplication;
import chenj.android.spriteweather.whole.MySharedPreference;


public class SettingActivity extends AppCompatActivity {

    private Button back_btn;//返回按键
    private TextView title_tv;//显示名称
    private TextView update_frequency;//显示名称
    private Button setting;//设置
    private ToggleView update_switch;//自动更新开关
    private List<String> dataList = new ArrayList<>();
    private String nowValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_setting);

        RelativeLayout update_frequency_layout = (RelativeLayout)findViewById(R.id.update_frequency_layout);
        back_btn = (Button)findViewById(R.id.area_bcak_btn);
        title_tv = (TextView)findViewById(R.id.area_title_text);
        setting = (Button)findViewById(R.id.setting);
        update_frequency = (TextView)findViewById(R.id.update_frequency);
        update_switch = (ToggleView)findViewById(R.id.update_switch);
        nowValue = MySharedPreference.getValue(MyApplication.PREFERENCE_UPDATE_FREQUENCY,"1");
        update_frequency.setText(nowValue + " 小时");
        update_switch.toggleSwitch(MySharedPreference.getValue(MyApplication.PREFERENCE_IS_UPDATE,true));
        update_switch.setOnStateChangedListener(new ToggleView.OnStateChangedListener() {
            @Override
            public void toggleToOn(ToggleView view) {
                MySharedPreference.putValue(MyApplication.PREFERENCE_IS_UPDATE, true);
                AutoUpdateService.startService(SettingActivity.this);
                update_switch.toggleSwitch(true);
            }

            @Override
            public void toggleToOff(ToggleView view) {
                MySharedPreference.putValue(MyApplication.PREFERENCE_IS_UPDATE, false);
                update_switch.toggleSwitch(false);
            }
        });


        title_tv.setText("设置");
        setting.setVisibility(View.GONE);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setDataList();
        update_frequency_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击更新频率
                showAlertDialog();
            }
        });

    }

    public static void startAction(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    public void showAlertDialog() {
        PickerDialog.Builder builder = new PickerDialog.Builder(this);
        builder.setTitle("更新频率");
        builder.setDataList(dataList);
        builder.setSelected(nowValue);
        builder.setPositiveButton("确定", new PickerDialog.SelectedListener() {
            @Override
            public void onSelected(Dialog dialog, String value) {
                dialog.dismiss();
                if(!nowValue.equals(value)){
                    MySharedPreference.putValue(MyApplication.PREFERENCE_UPDATE_FREQUENCY, value);
                    update_frequency.setText(value + " 小时");
                    nowValue = value;
                }
            }
        });

        builder.setNegativeButton("取消",new PickerDialog.CancelListener(){


            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        });

        PickerDialog dialog =  builder.create();
        dialog.show();
    }

    private void setDataList(){
        for(int i=1; i<7;i++){
            dataList.add(String.valueOf(i));
        }
    }
}
