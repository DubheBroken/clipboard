package com.rice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.rice.riceframe.R;

/**
 * 圆形View
 * Created by LGL on 2016/1/7.
 */
public class CircleView extends View {

    long color;

    //无参
    public CircleView(Context context) {
        super(context);
    }

    //有参
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        color = array.getColor(R.styleable.CircleView_cir_color, Color.BLACK);
        array.recycle();
    }

    public void setColor(long color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setColor((int) color);
        // 设置画笔的锯齿效果
        p.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, p);
    }
}