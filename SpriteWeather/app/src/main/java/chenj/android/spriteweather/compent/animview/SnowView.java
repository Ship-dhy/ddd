package chenj.android.spriteweather.compent.animview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import java.util.Random;

import chenj.android.spriteweather.R;

/**
 * Created by 72312 on 2017/12/5.
 */

public class SnowView extends BaseAnimView {
    public SnowView(Context context) {
        super(context);
        initShowElement();
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initShowElement();
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initShowElement();
    }

    private void initShowElement(){
        mWeatherBitmap   = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
    }
}
