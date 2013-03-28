package echozero.graphics;

import echozero.graphics.Primitive;
import echozero.math.Matrix3;

public class Color extends Primitive {
	private final double[] m_color;
	
	public Color(double r, double g, double b, double a) { 
		m_color = new double[4];
		
		m_color[0] = r;
		m_color[1] = g;
		m_color[2] = b;
		m_color[3] = a;
	}
	
	public void draw(Matrix3 tr, HostGraphics hg) {
		hg.set_color(m_color[0], m_color[1], m_color[2], m_color[3]);
	}
}
