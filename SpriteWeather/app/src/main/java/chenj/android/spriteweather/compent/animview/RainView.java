package chenj.android.spriteweather.compent.animview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by 72312 on 2017/12/5.
 */

public class RainView extends BaseAnimView {
    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawWeather(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        for (WeatherFlake flake : mFlakes) {
            //雨滴的宽度
            paint.setStrokeWidth(flake.getWidth());
            int startX = flake.getX();
            int startY = flake.getY();
            int stopX  = startX;
            int stopY  = startY  + flake.getHeight();
            canvas.drawLine(startX, startY, stopX, stopY, paint);
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
            y = flake.getY() + flake.getSpeedY();

            if ((x > mViewWidth + 20 || x < 0) || (y > mViewHeight + 20)) {
                x = new Random().nextInt(mViewWidth);
                y = 0;
            }
            flake.setY(y);
            flake.setX(x);
        }
    }

    //初始化显示的图案
    protected void initWeatherFlakes(){
        mFlakes = new WeatherFlake[mFlakeCount];
        boolean isRightDir = new Random().nextBoolean();
        for (int i = 0; i < mFlakes.length; i++) {
            mFlakes[i] = new WeatherFlake();
            mFlakes[i].setWidth((new Random().nextInt(2))+3);
            mFlakes[i].setHeight(new Random().nextInt(mMaxSize-mMinSize) + mMinSize);
            mFlakes[i].setX(new Random().nextInt(mViewWidth));
            mFlakes[i].setY(-(new Random().nextInt(mViewHeight)));
            mFlakes[i].setSpeedY(new Random().nextInt(4) + mSpeedY);
            if (isRightDir) {
                mFlakes[i].setSpeedX(new Random().nextInt(4) + mSpeedX);
            }
            else {
                mFlakes[i].setSpeedX(-(new Random().nextInt(4) + mSpeedX));
            }
        }
    }
}
