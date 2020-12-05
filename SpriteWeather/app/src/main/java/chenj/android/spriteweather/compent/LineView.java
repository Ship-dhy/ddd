package chenj.android.spriteweather.compent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义画一条横线
 * Created by Administrator on 2017/11/23 0023.
 */

public class LineView extends View{
    private static final int HEIGHT_SIZE = 6;
    private int widthSize;
    private int heightSize;

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(0xfff0f0f0);                    //设置画笔颜色
        paint.setStrokeWidth(HEIGHT_SIZE/2);
        canvas.drawLine(0, HEIGHT_SIZE/2, widthSize, HEIGHT_SIZE/2, paint);        //绘制直线
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, HEIGHT_SIZE);
    }
}
