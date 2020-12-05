package chenj.android.spriteweather.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import chenj.android.spriteweather.whole.MyApplication;

/**
 * UI工具类
 * Created by Administrator on 2017/11/23 0023.
 */

public class UiUtilty {
    public static int getStatusHeight(){
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = MyApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = MyApplication.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    //将状态栏背景图片与我们的app一致, 此功能在5.0系统后才支持
    public static void setStatusBackgroundWithActivity(Activity activity){
        if(Build.VERSION.SDK_INT >= 21){
            View decorview = activity.getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
