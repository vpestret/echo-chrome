package com.echogames.echochrome;

import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;


public class GameContext {
    private static final String TAG = "GameContext";

    private float mMapWidth = 2.0f;
    private float mMapHeight = 2.0f;
    public float mScale = 0f;
    public Unit [] mUnits;
    public boolean [] mUnitInCollision;
    public RectF mCurrentViewport = new RectF( 0f, 0f, mMapWidth * 3 / 4, mMapHeight * 3 / 4);
    
    public void restoreState( Bundle savedInstanceState)
    {
        mMapWidth = savedInstanceState.getFloat( "mMapWidth");
        mMapHeight = savedInstanceState.getFloat( "mMapHeight");
        mScale = savedInstanceState.getFloat( "mScale");
        int nUnits = savedInstanceState.getInt( "nUnits");
        mUnits = new Unit[ nUnits ];
        for ( int idx = 0; idx < nUnits; idx++ )
        {
            mUnits[ idx ] = new Unit( "Unit" + idx, savedInstanceState);
        }
        mUnitInCollision = savedInstanceState.getBooleanArray( "mUnitInCollision");
        mCurrentViewport = new RectF(
            savedInstanceState.getFloat( "mCurrentViewportLeft"),
            savedInstanceState.getFloat( "mCurrentViewportTop"),
            savedInstanceState.getFloat( "mCurrentViewportRight"),
            savedInstanceState.getFloat( "mCurrentViewportBottom"));
    }
    
    public void saveState( Bundle targetInstanceState)
    {
        targetInstanceState.putFloat( "mMapWidth", mMapWidth);
        targetInstanceState.putFloat( "mMapHeight", mMapHeight);
        targetInstanceState.putFloat( "mScale", mScale);
        int nUnits = mUnits.length;
        targetInstanceState.putInt( "nUnits", nUnits);
        for ( int idx = 0; idx < nUnits; idx++ )
        {
        	mUnits[ idx ].saveState( "Unit" + idx, targetInstanceState);
        }
        targetInstanceState.putBooleanArray( "mUnitInCollision", mUnitInCollision);
        targetInstanceState.putFloat( "mCurrentViewportLeft", mCurrentViewport.left);
        targetInstanceState.putFloat( "mCurrentViewportTop", mCurrentViewport.top);
        targetInstanceState.putFloat( "mCurrentViewportRight", mCurrentViewport.right);
        targetInstanceState.putFloat( "mCurrentViewportBottom", mCurrentViewport.bottom);
    }
    
    public float getMapWidth()
    {
        return mMapWidth;
    }
    public float getMapHeight()
    {
        return mMapHeight;
    }
    
    void genRamdomUnits( int nUnits, float r_min, float r_max)
    {
        mUnits = new Unit[ nUnits ];
        mUnitInCollision = new boolean [ nUnits ];
        for ( int idx = 0; idx < nUnits; idx++ )
        {
            mUnits[ idx ] = new Unit( ( float) ( Math.random() * ( mMapWidth - 2 * r_max) + r_max),
            ( float) ( Math.random() * ( mMapHeight - 2 * r_max) + r_max),
            ( float) ( Math.random() * ( r_max - r_min) + r_min),
            ( float) ( Math.random() * Math.PI * 2 - Math.PI));
            mUnitInCollision[ idx ] = false;
        }
        check_collision();
    }
    
    void check_collision()
    {
    	int nUnits = mUnits.length;
        float [] out_ratio = new float[ 1 ];
        
        for ( int idx = 0; idx < nUnits; idx++ )
        	mUnitInCollision[ idx ] = false;
        
        for ( int idx = 0; idx < nUnits - 1; idx++ )
        {
            for ( int idx2 = idx + 1; idx2 < nUnits ; idx2++ )
            {
                try 
                {
                    if ( mUnits[ idx ].collide( mUnits[ idx2 ], out_ratio) )
                    {
                        mUnitInCollision[ idx ] = true;
                        mUnitInCollision[ idx2 ] = true;
                    }
                } catch (CollisionException ce)
                {
                    Log.d( TAG, ce.getMessage());
                }
            }
        }
    }
    
    public void execute()
    {
    	for ( int idx = 0; idx < mUnits.length; idx++ )
    	{
    		mUnits[ idx ].execute();
    	}
    	check_collision();
    }
}
