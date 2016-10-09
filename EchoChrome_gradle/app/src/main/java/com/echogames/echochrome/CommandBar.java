package com.echogames.echochrome;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CommandBar extends View {
    private static final String TAG = "CommandBar";
    
    private Rect mContentRect = new Rect();
    private Paint mAxisPaint;
    private EchoChromeView mECV = null;
    private GameContext mGameContext = null;
    private int mPad = 5;
    private GestureDetectorCompat mGestureDetector;
    
    public void setGameContext( GameContext gc) {
        mGameContext = gc;
    }
    
    public void setEchoChromeView( EchoChromeView ecv) {
        mECV = ecv;
    }
    
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
        
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight()-1,
                getHeight() - getPaddingBottom()-1);
    }
    
    public void updateView()
    {
        ViewCompat.postInvalidateOnAnimation(this);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draws chart container
        canvas.drawRect(mContentRect, mAxisPaint);
        if ( mGameContext != null && mECV != null && mECV.mSelected != -1 )
        {
            ArrayList< Order > orders = mGameContext.mUnits[ mECV.mSelected ].orders;
            Iterator< Order > orders_i = orders.iterator();
            int order_idx = 0;
            int dir_x = mContentRect.width() > mContentRect.height() ? 1 : 0;
            int dir_y = 1 - dir_x;
            int mStride = dir_x * mContentRect.height() + dir_y * mContentRect.width();
            mAxisPaint.setTextSize( mStride - 2 * mPad);
            while (  orders_i.hasNext() )
            {
                Order order = orders_i.next();
                if ( order_idx == 0 )
                {
                    mAxisPaint.setStyle( Paint.Style.FILL_AND_STROKE);
                }
                canvas.drawText( order.getName(), mContentRect.left + mPad + mStride * dir_x * order_idx,
                                 mContentRect.bottom - mPad - mStride * dir_y * order_idx, mAxisPaint);
                mAxisPaint.setStyle( Paint.Style.STROKE);
                order_idx++;
            }
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mGestureDetector.onTouchEvent(event);
        return retVal || super.onTouchEvent(event);
    }
    
    private final GestureDetector.SimpleOnGestureListener mGestureListener
        = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onDown(MotionEvent e) {
            updateView();
            return true;
        }
        
        @Override     
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp: " + e.toString());
            updateOrders( e.getX(), e.getY());           
            updateView();
            return true;
        } 
    };
    
    private void updateOrders( float x, float y)
    {
        if ( mGameContext != null && mECV != null && mECV.mSelected != -1 )
        {
            int dir_x = mContentRect.width() > mContentRect.height() ? 1 : 0;
            int dir_y = 1 - dir_x;
            int mStride = dir_x * mContentRect.height() + dir_y * mContentRect.width();
            int idx = ( int) ( x / mStride) * dir_x +
                      ( int) ( ( mContentRect.height() - y) / mStride) * dir_y;
            ArrayList< Order > orders = mGameContext.mUnits[ mECV.mSelected ].orders;
            if ( 0 <= idx && idx < orders.size() )
            {
                orders.remove( idx);
            }
        }
    }
}
