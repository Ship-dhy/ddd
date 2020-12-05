package chenj.android.spriteweather.compent.animview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

import chenj.android.spriteweather.R;

/**
 * Created by 72312 on 2017/12/5.
 */

public abstract class BaseAnimView extends SurfaceView implements SurfaceHolder.Callback {
    //------------------------------------------------------添加以下变量，初始化默认值
    private SurfaceHolder mHolder;
    protected WeatherFlake[]   mFlakes;
    protected Bitmap mWeatherBitmap;
    protected int           mViewWidth  = 200;
    protected int           mViewHeight = 100;
    protected int           mFlakeCount = 20;
    protected int           mMinSize    = 50;
    protected int           mMaxSize    = 70;
    protected int           mSpeedX     = 10;
    protected int           mSpeedY     = 20;
    private boolean       mStart      = false;

    public BaseAnimView(Context context) {
        this(context, null);
    }

    public BaseAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //SurfaceHolder
        initHolder();
        setZOrderOnTop(true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Weather, defStyleAttr, 0);
        int cnt = array.getIndexCount();
        for (int i = 0; i < cnt; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.Weather_flakeCount:
                    mFlakeCount = array.getInteger(attr, 0);
                    break;
                case R.styleable.Weather_minSize:
                    mMinSize = array.getInteger(attr, 50);
                    break;
                case R.styleable.Weather_maxSize:
                    mMaxSize = array.getInteger(attr, 70);
                    break;
                case R.styleable.Weather_flakeSrc:
                    Integer srcId = array.getResourceId(attr, R.drawable.snow);
                    mWeatherBitmap   = BitmapFactory.decodeResource(getResources(), srcId);
                    break;

                case R.styleable.Weather_speedX:
                    mSpeedX = array.getInteger(attr, 10);
                    break;
                case R.styleable.Weather_speedY:
                    mSpeedY = array.getInteger(attr, 10);
                    break;
                default:
                    break;
            }
        }
        if (mMinSize > mMaxSize) {
            mMaxSize = mMinSize;
        }

        array.recycle();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initWeatherFlakes();
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        //在控件不可见时停止更新和绘制控件，避免CPU资源浪费
        mStart = (visibility == VISIBLE);
    }

    //并重写 onMeasure() 函数，测量SurfaceView的大小
    @SuppressLint("NewApi")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //--- measure the view's width
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            mViewWidth = (getPaddingStart() + mWeatherBitmap.getWidth() + getPaddingEnd());
        }

        //--- measure the view's height
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            mViewHeight = (getPaddingTop() + mWeatherBitmap.getHeight() + getPaddingBottom());
        }

        setMeasuredDimension(mViewWidth, mViewHeight);

    }

    private void initHolder() {
        mHolder = this.getHolder();
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mHolder.addCallback(this);
    }

    //初始化显示的图案
    protected void initWeatherFlakes(){
        mFlakes = new WeatherFlake[mFlakeCount];
        boolean isRightDir = new Random().nextBoolean();
        for (int i = 0; i < mFlakes.length; i++) {
            mFlakes[i] = new WeatherFlake();
            mFlakes[i].setWidth(new Random().nextInt(mMaxSize-mMinSize) + mMinSize);
            mFlakes[i].setHeight(mFlakes[i].getWidth());
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

    //通过SurfaceHolder 的lockCanvas()函数获取到画布，绘制完后再调用 unlockCanvasAndPost() 函数释放canvas并将缓冲区绘制的内容一次性绘制到canvas上
    private void drawView() {
        if (mHolder == null) {
            return;
        }
        Canvas canvas = mHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawWeather(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    protected void drawWeather(Canvas canvas) {
        Rect rect  = new Rect();
        Paint paint = new Paint();
        for (WeatherFlake flake : mFlakes) {
            rect.left   = flake.getX();
            rect.top    = flake.getY();
            rect.right  = rect.left + flake.getWidth();
            rect.bottom = rect.top  + flake.getHeight();
            canvas.drawBitmap(mWeatherBitmap, null, rect, paint);
        }
    }

    protected void updatePara(){
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

    public void start() {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        if (mStart) {
                            updatePara();
                            drawView();
                        }
                        Thread.sleep(20);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
