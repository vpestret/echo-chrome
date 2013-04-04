package echozero.graphics.primitives;

import echozero.graphics.host.HostGraphics;
import echozero.math.Matrix3;
import echozero.math.Vector2;

public class Polygon extends Primitive {
	protected Vector2[] m_pts;
	protected boolean m_fill;
	
	public Polygon() {}
	
	public Polygon(Vector2[] pts, boolean fill) { 
		super();
		m_pts = pts; 
		m_fill = fill;
	}

	public void draw(Matrix3 tr, HostGraphics hg) {
		int i;
		int np;
		int[] x;
		int[] y;
		
		np = m_pts.length;
		x = new int[np];
		y = new int[np];
		for(i = 0; i < np; ++i) {
			double[] xx;
			
			xx = tr.multiply_vector2h(m_pts[i]).get_x();
			x[i] = (int)xx[0];
			y[i] = (int)xx[1];
		}
		hg.poly(np, x, y, m_fill);
	}
}

