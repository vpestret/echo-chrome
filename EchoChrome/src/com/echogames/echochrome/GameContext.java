package com.echogames.echochrome;

import android.util.Log;


public class GameContext {
    private static final String TAG = "GameContext";

    private float mMapWidth = 2.0f;
    private float mMapHeight = 2.0f;
    public Unit [] mUnits;
    public boolean [] mUnitInCollision;
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
            ( float) ( Math.random() * Math.PI * 2));
            mUnitInCollision[ idx ] = false;
        }
        float [] out_ratio = new float[ 1 ];
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
}
