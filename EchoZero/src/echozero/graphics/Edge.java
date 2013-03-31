package echozero.graphics;

import echozero.math.Matrix3;
import echozero.math.Vector2;

public class Edge extends Primitive {
	private final Vector2 m_p1;
	private final Vector2 m_p2;
	
	public Edge(double x1, double y1, double x2, double y2) {
		super();
		m_p1 = new Vector2(x1, y1);
		m_p2 = new Vector2(x2, y2);
	}
	
	public void draw(Matrix3 tr, HostGraphics hg) {
		Vector2 v1;
		Vector2 v2;
		double[] x1;
		double[] x2;
		
		x1 = tr.multiply_vector2h(m_p1).get_x();
		x2 = tr.multiply_vector2h(m_p2).get_x();
		hg.line((int)x1[0], (int)x1[1], (int)x2[0], (int)x2[1]);
	}
}
