package echozero.graphics;

import echozero.math.Matrix3;
import echozero.util.DList;

public class PrimitiveList {
	private DList<Primitive> m_pl;
	
	public PrimitiveList() { m_pl = new DList<Primitive>(); }
		
	public void add(Primitive p) { m_pl.push_back(p); }

	public void draw(Matrix3 tr, HostGraphics hg) {
		for(m_pl.rw(); !m_pl.is_end(); m_pl.next()) { m_pl.get().draw(tr, hg); }
	}
}
