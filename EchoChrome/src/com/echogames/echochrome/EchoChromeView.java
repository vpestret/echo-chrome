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
    
    private float AXIS_X_MIN;
    private float AXIS_X_MAX;
    private float AXIS_Y_MIN;
    private float AXIS_Y_MAX;
    
	private RectF mCurrentViewport;
    private Rect mContentRect = new Rect();
    private float mScale;
	private Scroller mScroller;
    private Paint mDataPaint;
    private float mDataThickness = 2.0f;
    private int mDataColor = 0xff00aa00;
    private int mSelColor = 0xffaa0000;
    private int mCollideColor = 0xffaa00aa;
    private GestureDetectorCompat mGestureDetector;
    private GameContext mGameContext;
    private float mSelected = -1;
    
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
        
        mGameContext = new GameContext();
        AXIS_X_MIN = 0f;
        AXIS_X_MAX = mGameContext.getMapWidth();
        AXIS_Y_MIN = 0f;
        AXIS_Y_MAX = mGameContext.getMapHeight();
        mGameContext.genRamdomUnits( 20, ( float) 0.05, ( float) 0.1);
        
        mCurrentViewport = new RectF(AXIS_X_MIN, AXIS_Y_MIN,
					        		 AXIS_X_MIN + ( AXIS_X_MAX - AXIS_X_MIN) * 3 / 4,
					        		 AXIS_Y_MIN + ( AXIS_Y_MAX - AXIS_Y_MIN) * 3 / 4);
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
        mCurrentViewport.top  = AXIS_Y_MIN;
        mCurrentViewport.bottom = AXIS_Y_MIN + mCurrentViewport.width()*mContentRect.height()/mContentRect.width();
        mCurrentViewport.right = AXIS_X_MIN + mCurrentViewport.width();
        mCurrentViewport.left = AXIS_X_MIN;
        mScale = mContentRect.height()/mCurrentViewport.height();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    	//Log.d(TAG, "onDraw at " + mCurrentViewport.left + ", " + mCurrentViewport.top);
        // Clips the next few drawing operations to the content area
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mContentRect);

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

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);
    }        

    private void setViewportBottomLeft(float x, float y) {
        /**
         * Constrains within the scroll range. The scroll range is simply the viewport extremes
         * (AXIS_X_MAX, etc.) minus the viewport size. For example, if the extrema were 0 and 10,
         * and the viewport size was 2, the scroll range would be 0 to 8.
         */
        float curWidth = mCurrentViewport.width();
        float curHeight = mCurrentViewport.height();
        x = Math.max(AXIS_X_MIN, Math.min(x, AXIS_X_MAX - curWidth));
        y = Math.max(AXIS_Y_MIN + curHeight, Math.min(y, AXIS_Y_MAX));

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
    		for (int i = 0; i < mGameContext.mUnits.length; i++) {
    			float rad_sq = (x-mGameContext.mUnits[ i ].cx)*(x-mGameContext.mUnits[ i ].cx) +
    					       (y-mGameContext.mUnits[ i ].cy)*(y-mGameContext.mUnits[ i ].cy);
    			if ( rad_sq < mGameContext.mUnits[ i ].r*mGameContext.mUnits[ i ].r ) {
    				mSelected = i;
    			}
    		}
    	}
    }
    
    public void releaseSelection()
    {
    	mSelected = -1;
    	ViewCompat.postInvalidateOnAnimation(this);
    }
    
    /**
     * The gesture listener, used for handling simple gestures such as double touches, scrolls,
     * and flings.
     */
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
            // Scrolling uses math based on the viewport (as opposed to math using pixels).
            /**
             * Pixel offset is the offset in screen pixels, while viewport offset is the
             * offset within the current viewport. For additional information on surface sizes
             * and pixel offsets, see the docs for {@link computeScrollSurfaceSize()}. For
             * additional information about the viewport, see the comments for
             * {@link mCurrentViewport}.
             */
            float viewportOffsetX = distanceX * mCurrentViewport.width() / mContentRect.width();
            float viewportOffsetY = distanceY * mCurrentViewport.height() / mContentRect.height();
            setViewportBottomLeft( mCurrentViewport.left + viewportOffsetX,
                                   mCurrentViewport.bottom + viewportOffsetY);

            return true;
        }
    };

}
