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
		
	}
}
