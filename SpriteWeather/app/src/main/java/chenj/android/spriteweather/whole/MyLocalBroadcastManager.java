package chenj.android.spriteweather.whole;

import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class MyLocalBroadcastManager {

    private static LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());;

    public static LocalBroadcastManager getLocalBroadcastManager(){
        return localBroadcastManager;
    }
}
