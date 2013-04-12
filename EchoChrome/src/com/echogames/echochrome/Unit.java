package com.echogames.echochrome;

import java.util.ArrayList;

public class Unit extends RectEntity {
    // looks like circle with dedicated direction    
    public float cx;
    public float cy;
    public float r;
    public float dir; // direction is in radians
    
	public ArrayList<Order> orders;
    
    Unit(float cx, float cy, float r, float dir) {
        super();
        this.cx  = cx;
        this.cy  = cy;
        this.r   = r;    
        this.dir = dir;
        update_rect();
        orders = new ArrayList<Order>();
        orders.add( new Order( Order.ORDER_TURN));
        orders.add( new Order( Order.ORDER_SIDE));
        orders.add( new Order( Order.ORDER_RUN));
    }

    @Override
    public void update_rect() {
        this.left   = this.cx - this.r;
        this.right  = this.cx + this.r;
        this.top    = this.cy - this.r;
        this.bottom = this.cy + this.r;
    }
    
    public boolean collide( RectEntity rect, float[] out_ratio) throws CollisionException {
        out_ratio[0] = 0f;
        if ( rect instanceof Shoot )
        {            
            return ( ( Shoot) rect).collide(this, out_ratio);
        } else if ( rect instanceof Unit )
        {
            Unit it = ( Unit) rect;
            // optimization
            if ( !check_rect( it) )
            {
                return false;
            }
            // normal flow
            float s2 = ( it.cx - this.cx) * ( it.cx - this.cx) +
                       ( it.cy - this.cy) * ( it.cy - this.cy);
            if ( !(s2 < ( this.r + it.r) * ( this.r + it.r)) )
            {
                return false;
            }
            out_ratio[0] = 1f;
            return true;
        } else if ( rect instanceof Obstacle )
        {
            Obstacle it = ( Obstacle) rect;
            // optimization
            if ( !check_rect( it) )
            {
                return false;
            }
            boolean in_ne_corner = false;
            boolean in_se_corner = false;
            boolean in_sw_corner = false;
            boolean in_nw_corner = false;
            if ( this.cx > it.right )
            {
                if (this.cy < it.top)
                    in_ne_corner = true;
                else if (this.cy > it.bottom)
                    in_se_corner = true;
            }
            else if (this.cx < it.left)
            {
                if (this.cy < it.top)
                    in_nw_corner = true;
                else if (this.cy > it.bottom)
                    in_sw_corner = true;
            }
            if (in_nw_corner)
            {
                float s2_1 = (it.left-this.cx)*(it.left-this.cx) +
                       (it.top-this.cy)*(it.top-this.cy);
                if ( !(s2_1 < this.r*this.r) )
                    return false;
            }
            else if (in_ne_corner)
            {
                float s2_2 = (it.right-this.cx)*(it.right-this.cx) +
                       (it.top-this.cy)*(it.top-this.cy);
                if ( !(s2_2 < this.r*this.r) )
                    return false;
            }
            else if (in_se_corner)
            {
                float s2_3 = (it.right-this.cx)*(it.right-this.cx) +
                       (it.bottom-this.cy)*(it.bottom-this.cy);
                if ( !(s2_3 < this.r*this.r) )
                    return false;
            }
            else if (in_sw_corner)
            {
                float s2_4 = (it.left-this.cx)*(it.left-this.cx) +
                       (it.bottom-this.cy)*(it.bottom-this.cy);
                if ( !(s2_4 < this.r*this.r) )
                    return false;
            }
            out_ratio[0] = 1f;
            return true;
        } else
        {
            throw new CollisionException("collides of Unit other than ones with Shoot" +
                    " or with Unit" +
                    " or with Obstacle" +
                    " are not supported", this, rect);
        }
    }
}
