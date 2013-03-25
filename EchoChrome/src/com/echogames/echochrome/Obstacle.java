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
}
