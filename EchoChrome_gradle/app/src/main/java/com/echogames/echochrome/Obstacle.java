package com.echogames.echochrome;

public class Obstacle extends RectEntity {
    // looks like box    
    public float cx;
    public float cy;
    public float w;
    public float h; // height
    Obstacle(float cx, float cy, float w, float h) {
        super();
        this.cx = cx;
        this.cy = cy;
        this.w  = w;        
        this.h  = h;
        update_rect();
    }
    
    @Override
    public void update_rect() {
        this.left   = this.cx - this.w/2;
        this.right  = this.cx + this.w/2;
        this.top    = this.cy - this.h/2;
        this.bottom = this.cy + this.h/2;
    }
    
    public boolean collide( RectEntity rect, float[] out_ratio) throws CollisionException {
        out_ratio[0] = 0f;
        if ( rect instanceof Shoot )
        {            
            return ( ( Shoot) rect).collide(this, out_ratio);
        } else if ( rect instanceof Unit )
        {
            return ( ( Unit) rect).collide(this, out_ratio);
        } else if ( rect instanceof Obstacle )
        {
            Obstacle it = ( Obstacle) rect;
            // the only way
            if ( !check_rect( it) )
            {
                return false;
            }
            out_ratio[0] = 1f;
            return true;
        } else 
        {
            throw new CollisionException("collides of Obstacle other than ones with Shoot" +
                    " or with Unit" +
                    " or with Obstacle" +
                    " are not supported", this, rect);
        }
    }
}
