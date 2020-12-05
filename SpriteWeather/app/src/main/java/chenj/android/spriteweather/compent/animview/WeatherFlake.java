package chenj.android.spriteweather.compent.animview;

/**
 * 首先确定一片雪花所需要的参数：长、宽、在屏幕上的坐标、下落的水平/垂直速度....把它们封装到一个类里面
 * Created by 72312 on 2017/12/2.
 */

public class WeatherFlake {
    private int mWidth;
    private int mHeight;
    private int mX;
    private int mY;
    private int mSpeedX;
    private int mSpeedY;

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getSpeedX() {
        return mSpeedX;
    }

    public int getSpeedY() {
        return mSpeedY;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public void setX(int mX) {
        this.mX = mX;
    }

    public void setY(int mY) {
        this.mY = mY;
    }

    public void setSpeedX(int mSpeedX) {
        this.mSpeedX = mSpeedX;
    }

    public void setSpeedY(int mSpeedY) {
        this.mSpeedY = mSpeedY;
    }
}
