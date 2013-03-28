package echozero.graphics;

import echozero.math.Matrix3;
import echozero.math.Vector2;

public class Polygon extends Primitive {
	private final Vector2[] m_pts;
	private boolean m_fill;
	
	public Polygon(Vector2[] pts, boolean fill) { 
		super(); 
		m_pts = pts; 
		m_fill = fill;
	}

	public void draw(Matrix3 tr, HostGraphics hg) {
		
	}
}
