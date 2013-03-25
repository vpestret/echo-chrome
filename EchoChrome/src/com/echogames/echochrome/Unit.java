package com.echogames.echochrome;

public class Unit extends RectEntity {
	// looks like circle with dedicated direction	
	public float cx;
	public float cy;
	public float r;
	public float dir; // direction is in radians
	Unit(float cx, float cy, float r, float dir) {
		super();
		this.cx  = cx;
		this.cy  = cy;
		this.r   = r;	
		this.dir = dir;
		update_rect();
	}

	@Override
    public void update_rect() {
        this.left   = this.cx - this.r;
        this.right  = this.cx + this.r;
        this.top    = this.cy - this.r;
        this.bottom = this.cy + this.r;
    }
}
