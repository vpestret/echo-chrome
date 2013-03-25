package com.echogames.echochrome;

public class Shoot extends RectEntity {
	// looks like 2D vector	
	public float cx;
	public float cy;
	public float vx;
	public float vy;
	public float v;
	Shoot(float cx, float cy, float vx, float vy) {
		super();
		this.cx = cx;
		this.cy = cy;
		this.vx = vx;
		this.vy = vy;
		this.v  = ( float) Math.sqrt( vx  *  vx + vy  * vy);
		update_rect();
	}

	@Override
	public void update_rect() {
	    this.left   = Math.min(this.cx, this.cx+this.vx);
	    this.right  = Math.max(this.cx, this.cx+this.vx);
	    this.top    = Math.min(this.cy, this.cy+this.vy);
	    this.bottom = Math.max(this.cy, this.cy+this.vy);
	}
	
	public boolean collide( RectEntity rect, float[] out_ratio) throws CollisionException {
		out_ratio[0] = 0f;
		if ( rect instanceof Unit )
		{
		    Unit it = ( Unit) rect; 
            // optimization
            if ( !check_rect(it) )
            {
                return false;
            }
            // normal flow
            float s2 = ( it.cx - this.cx)  *  ( it.cx - this.cx) + ( it.cy - this.cy)  *  ( it.cy - this.cy);
            float r2 = it.r  *  it.r;
            if ( s2 < r2 )
            {
            	out_ratio[0] = 1f;
                return true;
            }
            // beginning is not inside the circle consider its end
            float v2 = this.v * this.v;
            // just squared v+r > s
            if ( !( v2 + 2  *  this.v  *  it.r + r2 > s2) ){
                return false;            	
            }
            // scalar multiplication
            float smv = ( it.cx - this.cx)  *  this.vx + ( it.cy - this.cy)  *  this.vy;
            if ( !(smv > 0) )
            {
            	return false;
            }
            // vector multiplication
            float sxv = ( it.cx - this.cx)  *  this.vy - ( it.cy - this.cy)  *  this.vx;
            float d2 = sxv  *  sxv / v2;
            // just squared d < r
            if ( !(d2 < r2) )
            {
                return false; // fly by w/o impact
            }
            float dti = ( float) Math.sqrt( s2 - d2) - ( float) Math.sqrt( r2 - d2); // distance to impact
            // last check is not squared
            if ( !(this.v > dti) )
            {
            	return false;
            }
            out_ratio[0] = (this.v - dti) / this.v;
            return true;
		} else if ( rect instanceof Obstacle )
		{
			Obstacle it = ( Obstacle) rect; 
            // optimization
            if ( !check_rect(it) )
            {
                return false;
            }
            boolean in_n_corner = false;
            boolean in_ne_corner = false;
            boolean in_e_corner = false;
            boolean in_se_corner = false;
            boolean in_s_corner = false;
            boolean in_sw_corner = false;
            boolean in_w_corner = false;
            boolean in_nw_corner = false;
            if ( this.cx > it.right )
            {
                if ( this.cy < it.top )
                    in_ne_corner = true;
                else if( this.cy > it.bottom )
                    in_se_corner = true;
                else
                    in_e_corner = true;
            } else if ( this.cx < it.top )
            {
                if ( this.cy < it.top )
                    in_nw_corner = true;
                else if ( this.cy > it.bottom )
                    in_sw_corner = true;
                else
                    in_w_corner = true;
            } else
            {
                if ( this.cy < it.top )
                    in_n_corner = true;
                else if ( this.cy > it.bottom )
                    in_s_corner = true;
                else
                {
                	out_ratio[0] = 1f;
                    return true;
                }
            }
            if ( in_n_corner )
            {
                if ( this.vx == 0.0 )
                {
                    out_ratio[0] = ( float) ( this.vy - ( it.top - this.cy)) / this.vy;
                    return true;
                }
                float ix = ( float) ( ( it.top - this.cy)  *  this.vx) / this.vy + this.cx;
                if ( it.right < ix && ix < it.right )
                {
                    out_ratio[0] = ( float) ( this.vy - ( it.top - this.cy)) / this.vy;
                    return true;
                }
                return false;
            } else if ( in_s_corner )
            {
                if (this.vx == 0.0)
                {
                    out_ratio[0] = ( float) (this.vy - (it.bottom - this.cy)) / this.vy;
                    return true;
                }
                float ix = ( float) ((it.bottom - this.cy) * this.vx) / this.vy + this.cx;
                if ( it.right < ix && ix < it.right )
                {
                    out_ratio[0] = ( float) (this.vy - (it.bottom - this.cy)) / this.vy;
                    return true;
                }
                return false;
            } else if ( in_w_corner )
            {
                if ( this.vy == 0.0 )
                {
                   out_ratio[0] = ( float) ( this.vx - ( it.right - this.cx)) / this.vx;
                   return true;
                }
                float iy = ( float) ( ( it.right - this.cx) * this.vy) / this.vx + this.cy;
                if ( it.top < iy && iy < it.bottom )
                {
                    out_ratio[0] = ( float) (this.vx - (it.right - this.cx)) / this.vx;
                }
                return false;
            } else if ( in_e_corner )
            {
                if ( this.vy == 0.0 )
                {
                    out_ratio[0] = ( float) ( this.vx - ( it.right - this.cx)) / this.vx;
                    return true;
                }
                float iy = ( float) ( ( it.right - this.cx) * this.vy) / this.vx + this.cy;
                if ( it.top < iy && iy < it.bottom )
                {
                    out_ratio[0] = ( float) ( this.vx - ( it.right - this.cx)) / this.vx;
                    return true;
                }
                return false;
            } else if ( in_ne_corner )
            {
            	float ix = ( float) ( ( it.top - this.cy) * this.vx) / this.vy + this.cx;
                if ( it.right < ix && ix < it.right )
                {
                    out_ratio[0] = ( float) ( this.vy - ( it.top - this.cy)) / this.vy;
                    return true; 
                }
                float iy = ( float) ( ( it.right - this.cx) * this.vy) / this.vx + this.cy;
                if ( it.top < iy && iy < it.bottom )
                {
                    out_ratio[0] = ( float) ( this.vx - ( it.right - this.cx)) / this.vx;
                    return true; 
                }
                return false;
            } else if ( in_se_corner )
            {
            	float ix = ( float) ( ( it.bottom - this.cy) * this.vx) / this.vy + this.cx;
                if ( it.right < ix && ix < it.right )
                {
                    out_ratio[0] = ( float) ( this.vy - ( it.bottom - this.cy)) / this.vy;
                    return true;
                }
                float iy = ( float) ( ( it.right - this.cx) * this.vy) / this.vx + this.cy;
                if ( it.top < iy && iy < it.bottom )
                {
                    out_ratio[0] = ( float) ( this.vx - ( it.right - this.cx)) / this.vx;
                    return true;
                }
                return false;
            } else if ( in_sw_corner )
            {
            	float ix = ( float) ( ( it.bottom - this.cy) * this.vx) / this.vy + this.cx;
                if ( it.right < ix && ix < it.right )
                {
                    out_ratio[0] = ( float) ( this.vy - ( it.bottom - this.cy)) / this.vy;
                    return true;
                }
                float iy = ( float) ( ( it.right - this.cx) * this.vy) / this.vx + this.cy;
                if ( it.top < iy && iy < it.bottom )
                {
                    out_ratio[0] = ( float) ( this.vx - ( it.right - this.cx)) / this.vx;
                    return true;
                }
                return false;
            } else if ( in_nw_corner )
            {
            	float ix = ( float) ( ( it.top - this.cy) * this.vx) / this.vy + this.cx;
                if ( it.right < ix && ix < it.right )
                {
                    out_ratio[0] = ( float) ( this.vy - ( it.top - this.cy)) / this.vy;
                    return true;
                }
                float iy = ( float) ( ( it.right - this.cx) * this.vy) / this.vx + this.cy;
                if ( it.top < iy && iy < it.bottom )
                {
                    out_ratio[0] = ( float) ( this.vx - ( it.right - this.cx)) / this.vx;
                    return true;
                }
                return false;
            }
            throw new CollisionException("control flow reached unexpectedly this point", this, rect);
		} else
		{
			throw new CollisionException("collides of Shoot other than ones with Unit" +
                             " or with Obstacle" +
                             " are not supported", this, rect);
		}
	}
	
}
