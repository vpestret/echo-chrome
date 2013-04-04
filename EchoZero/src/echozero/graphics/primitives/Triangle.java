package echozero.graphics.primitives;

import echozero.graphics.host.HostGraphics;
import echozero.math.Matrix3;
import echozero.math.Vector2;


public class Triangle extends Primitive {
	protected Vector2[] m_pts;
	protected boolean m_fill;
	
	public Triangle() {}
	
	public Triangle(Vector2[] pts, boolean fill) { 
		super();
		m_pts = pts; 
		m_fill = fill;
	}

	public Triangle(double x1, double y1, double x2, double y2, double x3, double y3, boolean fill) {
		super();
		m_pts = new Vector2[3];
		m_pts[0] = new Vector2(x1, y1);
		m_pts[1] = new Vector2(x2, y2);
		m_pts[2] = new Vector2(x3, y3);
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
