package echozero.graphics;

import echozero.math.Matrix3;

public class PrimitiveList {
	private Primitive m_head;
	private Primitive m_next;
	
	public PrimitiveList() {
		m_head = m_next = null;
	}
	
	public void add(Primitive p) {
		if(m_next == null) { m_head = m_next = p; }
		else {
			m_next.set_next(p);
			m_next = p;
		}
	}
	
	public Primitive get_head() { return m_head; }
	
	public void draw(Matrix3 tr, HostGraphics hg) {
		Primitive p;
		
		for(p = m_head; p != null; p = p.next()) { p.draw(tr, hg); }
	}
}
