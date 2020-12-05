package chenj.android.spriteweather.compent.animview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import java.util.Random;

import chenj.android.spriteweather.R;

/**
 * Created by 72312 on 2017/12/5.
 */

public class CloundView extends BaseAnimView {

    public CloundView(Context context) {
        super(context);
        initShowElement();
    }

    public CloundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initShowElement();
    }

    public CloundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initShowElement();
    }


    private void initShowElement(){
        //云的参数重置
        mFlakeCount = 1;
        mMinSize    = 900;
        mMaxSize    = 1000;
        mSpeedX     = 1;
        mSpeedY     = 1;
        mWeatherBitmap   = BitmapFactory.decodeResource(getResources(), R.drawable.clound_move);
    }

    @Override
    protected void initWeatherFlakes() {
        mFlakes = new WeatherFlake[mFlakeCount];
        boolean isRightDir = new Random().nextBoolean();
        for (int i = 0; i < mFlakes.length; i++) {
            mFlakes[i] = new WeatherFlake();
            mFlakes[i].setWidth(new Random().nextInt(mMaxSize-mMinSize) + mMinSize);
            mFlakes[i].setHeight(mFlakes[i].getWidth());

            mFlakes[i].setX(-mMinSize);
            mFlakes[i].setY(mViewHeight/10);
            mFlakes[i].setSpeedY(0);
            mFlakes[i].setSpeedX(mSpeedY);
        }
    }

    @Override
    protected void updatePara() {
        int x;
        int y;
        for (WeatherFlake flake : mFlakes) {
            if (flake == null) {
                break;
            }
            x = flake.getX() + flake.getSpeedX();
            y = flake.getY();
            if ((x > mViewWidth + 20) || x < -mMinSize) {
                x = -mMinSize;
            }
            flake.setY(y);
            flake.setX(x);
        }
    }
}
