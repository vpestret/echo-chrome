package com.echogames.echochrome;

import java.util.ArrayList;
import java.util.Iterator;
import android.os.Bundle;

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
    }
    
    Unit( String prefix, Bundle savedInstanceState)
    {
    	cx = savedInstanceState.getFloat( prefix + "cx");
        cy = savedInstanceState.getFloat( prefix + "cy");
        r = savedInstanceState.getFloat( prefix + "r");
        dir = savedInstanceState.getFloat( prefix + "dir");
        orders = new ArrayList<Order>();
        for ( int idx = 0; idx < savedInstanceState.getInt( prefix + "orders_num"); idx++ )
        {
        	orders.add( new Order( prefix + "order" + idx, savedInstanceState));
        }
    }
    
    public void saveState( String prefix, Bundle targetInstanceState)
    {
        targetInstanceState.putFloat( prefix + "cx", cx);
        targetInstanceState.putFloat( prefix + "cy", cy);
        targetInstanceState.putFloat( prefix + "r", r);
        targetInstanceState.putFloat( prefix + "dir", dir);
        targetInstanceState.putInt( prefix + "orders_num", orders.size());  
        int idx = 0;
    	Iterator< Order > orders_i = orders.iterator();
    	while ( orders_i.hasNext() )
    	{
    		orders_i.next().saveState( prefix + "order" + idx, targetInstanceState);	
    		idx++;
    	}   	
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
    
    private float maxVel = 0.01f;
    private float maxRad = ( float) Math.PI / 10;
    private double maxSin = Math.sin( maxRad);
    //private float minVel_sq = 0.000025f;
    
    public void execute()
    {
    	if ( orders.size() == 0 )
    		return;
    	
    	Order curr_order = orders.get( 0);
		float vect_x = curr_order.getX() - cx;
		float vect_y = curr_order.getY() - cy;
		double vect = Math.sqrt( ( vect_x * vect_x + vect_y * vect_y) );
		boolean move_finished = false;
		boolean turn_finished = false;
		
    	if ( curr_order.getType() == Order.ORDER_SIDE || curr_order.getType() == Order.ORDER_RUN )
    	{
    		double tau = vect / maxVel;
    		if ( tau  > 1.0 )
    		{
    			cx += ( float) vect_x / tau;
    			cy += ( float) vect_y / tau;
    		} else {
    			cx = curr_order.getX();
    			cy = curr_order.getY();
    			if ( curr_order.getType() != Order.ORDER_RUN )
    				orders.remove( 0);
    			else
    				move_finished = true;
    		}
    	}
    	
    	if ( curr_order.getType() == Order.ORDER_TURN || curr_order.getType() == Order.ORDER_RUN )
    	{
    		double sin_dir = Math.sin( dir);
    		double cos_dir = Math.cos( dir);
    		double sin_vect = vect_x / vect;
    		double cos_vect = vect_y / vect;
    		double sin_diff = cos_vect * cos_dir - sin_vect * sin_dir;
    		double cos_diff = sin_vect * cos_dir + cos_vect * sin_dir;
    		if ( cos_diff < 0 || sin_diff > maxSin)
    		{
    			dir += Math.signum( sin_diff) * maxRad;
    		} else
    		{
    			dir = ( float) Math.atan2( vect_y, vect_x);
       			if ( curr_order.getType() != Order.ORDER_RUN )
    				orders.remove( 0);
    			else
    				turn_finished = true;
    		}		
    	}
    	
    	if ( curr_order.getType() == Order.ORDER_RUN && move_finished && turn_finished )
    		orders.remove( 0);    	
    }
}
