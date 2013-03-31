package com.echogames.echochrome;

import android.graphics.RectF;

public class RectEntity extends RectF {
    // Basic rectangle intended for optimization in collision detection
    // re_x, re_y - upper left corner
    // re_r - right border
    // re_b - bottom
    RectEntity() {}
    RectEntity( float re_x, float re_y, float re_r, float re_b)
    {
        super(re_x, re_y, re_r, re_b);
    }
    public boolean check_rect(RectF it)
    {
        if ( this.left > it.right || it.left > this.right ||
             this.top > it.bottom || it.top > this.bottom )
            return false;
        else
            return true;        
    }
    public void update_rect()
    {
        this.left   = 0;
        this.top    = 0;
        this.right  = 0;
        this.bottom = 0;
    }
}
