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
		this.v  = (float) Math.sqrt((double)(vx*vx + vy*vy));
		update_rect();
	}

		@Override
	public void update_rect() {
	    this.left   = Math.min(this.cx, this.cx+this.vx);
	    this.right  = Math.max(this.cx, this.cx+this.vx);
	    this.top    = Math.min(this.cy, this.cy+this.vy);
	    this.bottom = Math.max(this.cy, this.cy+this.vy);
	}
	
}
