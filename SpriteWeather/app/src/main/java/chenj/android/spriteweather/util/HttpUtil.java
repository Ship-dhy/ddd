package chenj.android.spriteweather.util;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * 网络处理工具类
 * Created by Administrator on 2017/11/16 0016.
 */

public class HttpUtil {
    /**
     * 使用Okhttp发送Http请求
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, Callback callback ){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
