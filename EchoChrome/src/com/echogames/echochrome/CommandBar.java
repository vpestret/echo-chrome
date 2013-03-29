package com.echogames.echochrome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class CommandBar extends View {
    //private static final String TAG = "CommandBar";
    
    private Rect mContentRect = new Rect();
    private Paint mAxisPaint;
    
    public CommandBar(Context context) {
        this(context, null, 0);
    }
    
    public CommandBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public CommandBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        mAxisPaint = new Paint();
        mAxisPaint.setStrokeWidth(1.0f);
        mAxisPaint.setColor(0xff000000);
        mAxisPaint.setStyle(Paint.Style.STROKE);
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight()-1,
                getHeight() - getPaddingBottom()-1);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draws chart container
        canvas.drawRect(mContentRect, mAxisPaint);
    }
}
