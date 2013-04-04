package echozero.graphics.primitives;

import echozero.graphics.Drawable;
import echozero.graphics.host.HostGraphics;
import echozero.math.Matrix3;

public class Primitive implements Drawable {
	private Primitive m_next;
	
	public Primitive() { m_next = null; }
	
	public Primitive next() { return m_next; }
	public void set_next(Primitive p) { m_next = p; }
	
	public void draw(Matrix3 tr, HostGraphics hg) {}
}
