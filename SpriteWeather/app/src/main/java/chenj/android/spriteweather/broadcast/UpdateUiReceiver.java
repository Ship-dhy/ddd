package chenj.android.spriteweather.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * b本地广播，通知更新天气UI
 * Created by Administrator on 2017/11/24 0024.
 */


public class UpdateUiReceiver extends BroadcastReceiver {
    private UiDataChangedListener listener;

    public UpdateUiReceiver(UiDataChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        listener.onUiChanged();
    }


    public interface UiDataChangedListener {
        public void onUiChanged();
    }
}
