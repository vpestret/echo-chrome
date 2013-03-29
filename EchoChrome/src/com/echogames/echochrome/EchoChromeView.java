package com.echogames.echochrome;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class EchoChromeView extends View {
    private static final String TAG = "EchoChromeView";
    
    private float MAX_POLE_RATIO = 0.25f;
    private float AXIS_X_MIN;
    private float AXIS_X_MAX;
    private float AXIS_Y_MIN;
    private float AXIS_Y_MAX;
    private float mInitialScale = 500f;
    
    private RectF mCurrentViewport;
    private Rect mContentRect = new Rect();
    private float mScale;
    private Scroller mScroller;
    private Paint mDataPaint;
    private float mDataThickness = 2.0f;
    private int mDataColor = 0xff00aa00;
    private int mSelColor = 0xffaa0000;
    private int mCollideColor = 0xffaa00aa;
    private int mPoleColor = 0xff808080;
    private GestureDetectorCompat mGestureDetector;
    private GameContext mGameContext;
    private float mSelected = -1;
    private float mPoleW = 0f;
    private float mPoleH = 0f;
    
    public EchoChromeView(Context context) {
        this(context, null, 0);
    }
    
    public EchoChromeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public EchoChromeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        mScroller = new Scroller(context);
       
        mDataPaint = new Paint();
        mDataPaint.setStrokeWidth(mDataThickness);
        mDataPaint.setColor(mDataColor);
        mDataPaint.setStyle(Paint.Style.STROKE);
        mDataPaint.setAntiAlias(true);
        
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
        
        // default values will be overwritten by setGameContext
        AXIS_X_MIN = 0f;
        AXIS_X_MAX = 1f;
        AXIS_Y_MIN = 0f;
        AXIS_Y_MAX = 1f;
        
        mCurrentViewport = new RectF(AXIS_X_MIN, AXIS_Y_MIN,
                                     AXIS_X_MIN + ( AXIS_X_MAX - AXIS_X_MIN) * 3 / 4,
                                     AXIS_Y_MIN + ( AXIS_Y_MAX - AXIS_Y_MIN) * 3 / 4);
    }
    
    public void setGameContext( GameContext gc) {
        mGameContext = gc;
        AXIS_X_MIN = 0f;
        AXIS_X_MAX = mGameContext.getMapWidth();
        AXIS_Y_MIN = 0f;
        AXIS_Y_MAX = mGameContext.getMapHeight();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        // make same proportions as for content rectangle to maintain circle shape
        if ( mGameContext != null )
        {
            mCurrentViewport.top  = mGameContext.mCurrentViewport.top;
            mCurrentViewport.left = mGameContext.mCurrentViewport.left;
            if (mGameContext.mScale != 0f)
                mScale = mGameContext.mScale;
            else
                mScale = mInitialScale;
            //Log.d( TAG, "Scale = " + mScale);
        } else
        {
            mCurrentViewport.top  = AXIS_Y_MIN;
            mCurrentViewport.left = AXIS_X_MIN;
            mScale = mInitialScale;
        }
        mCurrentViewport.bottom = mCurrentViewport.top + mContentRect.height() / mScale;
        mCurrentViewport.right = mCurrentViewport.left + mContentRect.width() / mScale;
        
        mPoleW = MAX_POLE_RATIO * mCurrentViewport.width();
        mPoleH = MAX_POLE_RATIO * mCurrentViewport.height();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.d(TAG, "onDraw at " + mCurrentViewport.left + ", " + mCurrentViewport.top);
        // Clips the next few drawing operations to the content area
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mContentRect);

        if ( mGameContext != null )
        {
            int nUnits = mGameContext.mUnits.length;
            for ( int idx = 0; idx < nUnits; idx++ )
            {
                if ( idx == mSelected )
                    mDataPaint.setColor(mSelColor);
                else if ( mGameContext.mUnitInCollision[ idx ] )
                    mDataPaint.setColor(mCollideColor);
                else
                    mDataPaint.setColor(mDataColor);
                
                float cx = ( mGameContext.mUnits[ idx ].cx - mCurrentViewport.left) * mScale;
                float cy = ( mGameContext.mUnits[ idx ].cy - mCurrentViewport.top) * mScale;
                float r =   mGameContext.mUnits[ idx ].r * mScale;
                double dir = ( double) mGameContext.mUnits[ idx ].dir;
                canvas.drawCircle( cx, cy, r, mDataPaint);
                canvas.drawLine( cx, cy, cx + ( float) Math.cos( dir) * r ,
                                 cy + ( float) Math.sin( dir) * r , mDataPaint);
            }
        }
        
        float poleLeft = AXIS_X_MIN - mCurrentViewport.left;
        float poleRight = mCurrentViewport.right - AXIS_X_MAX;
        float poleTop = AXIS_Y_MIN - mCurrentViewport.top;
        float poleBottom = mCurrentViewport.bottom - AXIS_Y_MAX;
        
        if ( poleLeft > 0 )
        {
            mDataPaint.setColor( mPoleColor);
            mDataPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect( mContentRect.left, mContentRect.top, mContentRect.left + poleLeft * mScale, mContentRect.bottom, mDataPaint);
            mDataPaint.setStyle(Paint.Style.STROKE);
        }
        
        if ( poleRight > 0 )
        {
            mDataPaint.setColor( mPoleColor);
            mDataPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect( mContentRect.right - poleRight * mScale, mContentRect.top, mContentRect.right, mContentRect.bottom, mDataPaint);
            mDataPaint.setStyle(Paint.Style.STROKE);
        }
        
        if ( poleTop > 0 )
        {
            mDataPaint.setColor( mPoleColor);
            mDataPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect( mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.top + poleTop * mScale, mDataPaint);
            mDataPaint.setStyle(Paint.Style.STROKE);
        }
        
        if ( poleBottom > 0 )
        {
            mDataPaint.setColor( mPoleColor);
            mDataPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect( mContentRect.left, mContentRect.bottom - poleBottom * mScale, mContentRect.right, mContentRect.bottom, mDataPaint);
            mDataPaint.setStyle(Paint.Style.STROKE);
        }

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);
    }        

    private void setViewportBottomLeft(float x, float y) {
        float curWidth = mCurrentViewport.width();
        float curHeight = mCurrentViewport.height();
        
        x = Math.max(AXIS_X_MIN - mPoleW, Math.min(x, AXIS_X_MAX + mPoleW - curWidth));
        y = Math.max(AXIS_Y_MIN - mPoleH + curHeight, Math.min(y, AXIS_Y_MAX + mPoleH));

        mCurrentViewport.set(x, y - curHeight, x + curWidth, y);
        ViewCompat.postInvalidateOnAnimation(this);
    }        

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mGestureDetector.onTouchEvent(event);
        return retVal || super.onTouchEvent(event);
    }
    
    private boolean hitTest(float x, float y, PointF dest) {
        if (!mContentRect.contains((int) x, (int) y)) {
            return false;
        }

        dest.set( mCurrentViewport.left + x / mScale,
                  mCurrentViewport.top  + y / mScale);
        return true;
     }
    
    private void updateSel(float x, float y) {
        PointF point = new PointF();
        if ( hitTest( x,y, point) && mSelected == -1)
        {
            x = point.x;
            y = point.y;
            mSelected = -1; // possible useless
            if ( mGameContext != null )
            {
                for (int i = 0; i < mGameContext.mUnits.length; i++) {
                    float rad_sq = (x-mGameContext.mUnits[ i ].cx)*(x-mGameContext.mUnits[ i ].cx) +
                                   (y-mGameContext.mUnits[ i ].cy)*(y-mGameContext.mUnits[ i ].cy);
                    if ( rad_sq < mGameContext.mUnits[ i ].r*mGameContext.mUnits[ i ].r ) {
                        mSelected = i;
                    }
                }
            }
        }
    }
    
    public void releaseSelection()
    {
        mSelected = -1;
        ViewCompat.postInvalidateOnAnimation(this);
    }
    
    private final GestureDetector.SimpleOnGestureListener mGestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            mScroller.forceFinished(true);
            ViewCompat.postInvalidateOnAnimation(EchoChromeView.this);
            return true;
        }
        
        @Override     
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp: " + e.toString());
            updateSel(e.getX(), e.getY());
            ViewCompat.postInvalidateOnAnimation(EchoChromeView.this);
            return true;
        } 
        
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // So convert from pixels to view coordinates
            setViewportBottomLeft( mCurrentViewport.left + distanceX / mScale,
                                   mCurrentViewport.bottom + distanceY / mScale);
            
            if ( mGameContext != null )
            {
                mGameContext.mCurrentViewport.set(mCurrentViewport.left, mCurrentViewport.top, mCurrentViewport.right, mCurrentViewport.bottom);
                mGameContext.mScale = mScale;
            }

            return true;
        }
    };

}
