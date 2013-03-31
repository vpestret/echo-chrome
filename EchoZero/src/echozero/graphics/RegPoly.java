package echozero.graphics;

import echozero.math.Vector2;

public class RegPoly extends Polygon {
	public RegPoly(int np, double R, boolean fill) {
		super();
		int i;
		Vector2[] pts;
		
		pts = new Vector2[np + 1];
		for(i = 0; i < np + 1; ++i) {
			double a;
			
			a = (2 * Math.PI * i / np);
			pts[i] = new Vector2(R * Math.cos(a), R * Math.sin(a));
		}
		m_pts = pts;
		m_fill = fill;
	}
}
