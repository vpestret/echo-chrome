package com.echogames.echochrome;


import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
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
    private int mMenuColor = 0xff000000;
    private int mUnitColor = 0xff00aa00;
    private int mSelColor = 0xffaa0000;
    private int mCollideColor = 0xffaa00aa;
    private int mPoleColor = 0xff808080;
    private GestureDetectorCompat mGestureDetector;
    private GameContext mGameContext;
    private CommandBar mCB;
    public  int mSelected = -1;
    private float mPoleW = 0f;
    private float mPoleH = 0f;
    private boolean mMenuVisible = false;
    private float mMenuCX = 0;
    private float mMenuCY = 0;
    private float mMenuTCX = 0;
    private float mMenuTCY = 0;
    private float mMenuSCX = 0;
    private float mMenuSCY = 0;
    private float mMenuRCX = 0;
    private float mMenuRCY = 0;
    private float mMenuRadius = 30;
    private float mMenuItemsRadius = 25;
    private Timer mTimer;
    
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
        mDataPaint.setStrokeWidth( mDataThickness);
        mDataPaint.setColor( mMenuColor);
        mDataPaint.setStyle( Paint.Style.STROKE);
        mDataPaint.setAntiAlias( true);
        
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
        
        // default values will be overwritten by setGameContext
        AXIS_X_MIN = 0f;
        AXIS_X_MAX = 1f;
        AXIS_Y_MIN = 0f;
        AXIS_Y_MAX = 1f;
        
        mCurrentViewport = new RectF(AXIS_X_MIN, AXIS_Y_MIN,
                                     AXIS_X_MIN + ( AXIS_X_MAX - AXIS_X_MIN) * 3 / 4,
                                     AXIS_Y_MIN + ( AXIS_Y_MAX - AXIS_Y_MIN) * 3 / 4);
        
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate( mTimerTask, 50, 100);
    }
    
    public void setGameContext( GameContext gc) {
        mGameContext = gc;
        AXIS_X_MIN = 0f;
        AXIS_X_MAX = mGameContext.getMapWidth();
        AXIS_Y_MIN = 0f;
        AXIS_Y_MAX = mGameContext.getMapHeight();
    }
    
    public void setCommandBar( CommandBar cb) {
        mCB = cb;
    }
    
    private PointF tmp_disp2map = new PointF( 0f, 0f);
    private Point  tmp_map2disp = new Point( 0, 0);
    
    public PointF disp2map( int x, int y)
    {
        tmp_disp2map.x = ( float) x / mScale + mCurrentViewport.left;
        tmp_disp2map.y = ( float) y / mScale + mCurrentViewport.top;
        return tmp_disp2map;
    }
    
    public Point map2disp( float x, float y)
    {
        tmp_map2disp.x = Math.round( ( x - mCurrentViewport.left) * mScale);
        tmp_map2disp.y = Math.round( ( y - mCurrentViewport.top) * mScale);
        return tmp_map2disp;
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
        PointF tmpF = disp2map( mContentRect.width(), mContentRect.height());
        mCurrentViewport.right  = tmpF.x;
        mCurrentViewport.bottom = tmpF.y;        
        
        mPoleW = MAX_POLE_RATIO * mCurrentViewport.width();
        mPoleH = MAX_POLE_RATIO * mCurrentViewport.height();
    }
    
    @Override
    protected void onDraw( Canvas canvas) {
        super.onDraw( canvas);

        //Log.d(TAG, "onDraw at " + mCurrentViewport.left + ", " + mCurrentViewport.top);
        // Clips the next few drawing operations to the content area
        int clipRestoreCount = canvas.save();
        canvas.clipRect( mContentRect);

        if ( mGameContext != null )
        {
            int nUnits = mGameContext.mUnits.length;
            for ( int idx = 0; idx < nUnits; idx++ )
            {
                if ( idx == mSelected )
                    mDataPaint.setColor( mSelColor);
                else if ( mGameContext.mUnitInCollision[ idx ] )
                    mDataPaint.setColor( mCollideColor);
                else
                    mDataPaint.setColor( mUnitColor);
                
                Point tmp = map2disp( mGameContext.mUnits[ idx ].getCX(), mGameContext.mUnits[ idx ].getCY());
                float cx = ( float) tmp.x;
                float cy = ( float) tmp.y;
                float r =   mGameContext.mUnits[ idx ].getR() * mScale;
                double dir = ( double) mGameContext.mUnits[ idx ].getDir();
                canvas.drawCircle( cx, cy, r, mDataPaint);
                canvas.drawLine( cx, cy, cx + ( float) Math.cos( dir) * r ,
                                 cy + ( float) Math.sin( dir) * r , mDataPaint);
            }
        }
        
        float poleLeft = AXIS_X_MIN - mCurrentViewport.left;
        float poleRight = mCurrentViewport.right - AXIS_X_MAX;
        float poleTop = AXIS_Y_MIN - mCurrentViewport.top;
        float poleBottom = mCurrentViewport.bottom - AXIS_Y_MAX;
        
        mDataPaint.setColor( mPoleColor);
        mDataPaint.setStyle( Paint.Style.FILL);
        if ( poleLeft > 0 )
        {
            canvas.drawRect( mContentRect.left, mContentRect.top, mContentRect.left + poleLeft * mScale, mContentRect.bottom, mDataPaint);
        }
        
        if ( poleRight > 0 )
        {
            canvas.drawRect( mContentRect.right - poleRight * mScale, mContentRect.top, mContentRect.right, mContentRect.bottom, mDataPaint);
        }
        
        if ( poleTop > 0 )
        {
            canvas.drawRect( mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.top + poleTop * mScale, mDataPaint);
        }
        
        if ( poleBottom > 0 )
        {
            canvas.drawRect( mContentRect.left, mContentRect.bottom - poleBottom * mScale, mContentRect.right, mContentRect.bottom, mDataPaint);            
        }
        mDataPaint.setStyle( Paint.Style.STROKE);
                
        if ( mMenuVisible )
        {
            mDataPaint.setColor( mMenuColor);
            canvas.drawCircle( mMenuCX, mMenuCY, mMenuRadius, mDataPaint);
            
            // Turn menu item
            canvas.drawCircle( mMenuTCX, mMenuTCY, mMenuItemsRadius, mDataPaint);
            canvas.drawText( "T", mMenuTCX, mMenuTCY, mDataPaint);
            // Side step menu item
            canvas.drawCircle( mMenuSCX, mMenuSCY, mMenuItemsRadius, mDataPaint);
            canvas.drawText( "S", mMenuSCX, mMenuSCY, mDataPaint);
            // Run menu item
            canvas.drawCircle( mMenuRCX, mMenuRCY, mMenuItemsRadius, mDataPaint);
            canvas.drawText( "R", mMenuRCX, mMenuRCY, mDataPaint);            
        }

        // Removes clipping rectangle
        canvas.restoreToCount( clipRestoreCount);
    }        

    private void setViewportTopLeft( float x, float y) {
        float curWidth = mCurrentViewport.width();
        float curHeight = mCurrentViewport.height();
        
        x = Math.max(AXIS_X_MIN - mPoleW, Math.min(x, AXIS_X_MAX + mPoleW - curWidth));
        y = Math.max(AXIS_Y_MIN - mPoleH, Math.min(y, AXIS_Y_MAX + mPoleH - curHeight));

        mCurrentViewport.set(x, y, x + curWidth, y + curHeight);
        ViewCompat.postInvalidateOnAnimation(this);
    }        

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mGestureDetector.onTouchEvent(event);
        return retVal || super.onTouchEvent(event);
    }
    
    private boolean hitTest( float x, float y, PointF dest) {
        if ( !mContentRect.contains( ( int) x, ( int) y)) {
            return false;
        }

        PointF tmpF = disp2map( ( int) x, ( int) y);
        dest.set( tmpF.x, tmpF.y);
        return true;
     }
    
    private void updateSel( float x, float y) {
        PointF point = new PointF();
        if ( hitTest( x,y, point) && mSelected == -1 )
        {
            x = point.x;
            y = point.y;
            mSelected = -1; // possible useless
            if ( mGameContext != null )
            {
                for ( int i = 0; i < mGameContext.mUnits.length; i++ ) {
                    float rad_sq = ( x - mGameContext.mUnits[ i ].getCX()) * ( x - mGameContext.mUnits[ i ].getCX()) +
                                   ( y - mGameContext.mUnits[ i ].getCY()) * ( y - mGameContext.mUnits[ i ].getCY());
                    if ( rad_sq < mGameContext.mUnits[ i ].getR() * mGameContext.mUnits[ i ].getR() ) {
                        mSelected = i;
                    }
                }
            }
        }
    }
    
    private void updateMenu( float x, float y) {
        int order_type = Order.ORDER_ERR;
      
        if ( mSelected != -1 && mMenuVisible ){            
            // detect button to hit
            float rad_sq = ( x - mMenuTCX) * ( x - mMenuTCX) + ( y - mMenuTCY) * ( y - mMenuTCY);
            if ( rad_sq < mMenuItemsRadius * mMenuItemsRadius )
                order_type = Order.ORDER_TURN;
            rad_sq = ( x - mMenuSCX) * ( x - mMenuSCX) + ( y - mMenuSCY) * ( y - mMenuSCY);
            if ( rad_sq < mMenuItemsRadius * mMenuItemsRadius )
                order_type = Order.ORDER_SIDE;
            rad_sq = ( x - mMenuRCX) * ( x - mMenuRCX) + ( y - mMenuRCY) * ( y - mMenuRCY);
            if ( rad_sq < mMenuItemsRadius * mMenuItemsRadius )
                order_type = Order.ORDER_RUN;
            
            if ( Order.ORDER_ERR != order_type && mGameContext != null)
            {
                PointF tmpF = disp2map( ( int) mMenuCX, ( int) mMenuCY);
                mGameContext.mUnits[ mSelected ].orders.add( new Order( order_type, tmpF.x, tmpF.y));
            }
        }
        if ( mSelected != -1 && order_type == Order.ORDER_ERR) {
            mMenuVisible = true;
            mMenuCX = x;
            mMenuCY = y;
            float item_distance = mMenuRadius + mMenuItemsRadius;
            mMenuTCX = mMenuCX - item_distance * ( float) Math.cos( Math.PI / 6);
            mMenuTCY = mMenuCY + item_distance * ( float) Math.sin( Math.PI / 6);
            mMenuSCX = mMenuCX;
            mMenuSCY = mMenuCY - item_distance;
            mMenuRCX = mMenuCX + item_distance * ( float) Math.cos( Math.PI / 6);
            mMenuRCY = mMenuCY + item_distance * ( float) Math.sin( Math.PI / 6);
        }
    }
    
    public void releaseSelection()
    {
        mSelected = -1;
        mMenuVisible = false;
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
            updateMenu( e.getX(), e.getY());
            updateSel( e.getX(), e.getY());           
            ViewCompat.postInvalidateOnAnimation(EchoChromeView.this);
            mCB.updateView();
            return true;
        } 
        
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // So convert from pixels to view coordinates
            PointF tmpF = disp2map( ( int) distanceX, ( int) distanceY);
            setViewportTopLeft( tmpF.x, tmpF.y);
            
            if ( mGameContext != null )
            {
                mGameContext.mCurrentViewport.set(mCurrentViewport.left, mCurrentViewport.top, mCurrentViewport.right, mCurrentViewport.bottom);
                mGameContext.mScale = mScale;
            }

            return true;
        }
    };
    
    class AnimationTimerTask extends TimerTask 
    {
        private int counter;

        public void run() {
            if ( mGameContext != null )
            {
                mGameContext.execute();
                ViewCompat.postInvalidateOnAnimation(EchoChromeView.this);
                mCB.updateView();
            }                
            if (counter % 10 == 0)
                Log.d( TAG, "Counter = " + counter);
            counter++;
        }
    };
    
    private AnimationTimerTask mTimerTask = new AnimationTimerTask();    
    
    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        
        mTimer.cancel();
    }
}
