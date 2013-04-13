package echoone.geom;

public class Edge {
	private final Point m_p1;
	private final Point m_p2;
	private final int m_dx;
	private final int m_dy;
	
	private final boolean m_pnt;
	private final boolean m_hor;
	private final boolean m_ver;
	private final boolean m_45deg;
	private final boolean m_135deg;
	
	public Edge(int x1, int y1, int x2, int y2) {
		m_p1 = new Point(x1, y1);
		m_p2 = new Point(x2, y2);
		m_dx = x2 - x1;
		m_dy = y2 - y1;
		m_hor = m_p1.y() == m_p2.y();
		m_ver = m_p1.x() == m_p2.x();
		m_pnt = m_hor && m_ver;		
		m_45deg = m_dx == m_dy;
		m_135deg = m_dx == -m_dy;
	}
	
	public Point p1() { return m_p1; }
	public Point p2() { return m_p2; }
	
	public boolean is_point() { return m_pnt; }
	public boolean is_horiz() { return m_hor; }
	public boolean is_vert() { return m_ver; }
	public boolean is_45deg() { return m_45deg; }
	public boolean is_135deg() { return m_135deg; }
	
	public double len2d() { 
		return ((double)m_dx * m_dx + (double)m_dy * m_dy);  
	}
	
	public boolean cross(Point p, boolean touch) {
		if(m_pnt && touch) {
			if(m_p1.compare(p) == 0) { return true; }
			return false;
		}
		
		if((long)(p.x() - m_p1.x()) * m_dy != (long)(p.y() - m_p1.y()) * m_dx) { return false; }
		
		if(m_ver) { /* vertical */
			int d;
			
			d = p.y() - m_p1.y();
			return ((d > 0 || (d == 0 && touch)) && (d < m_dy || (d == m_dy && touch))); 
		} else {
			int d;
			
			d = p.x() - m_p1.x();
			return ((d > 0 || (d == 0 && touch)) && (d < m_dx || (d == m_dx && touch)));
		}
	}
	
	public Point[] intersect(Edge e, boolean touch) {
		Point[] res;
		int xc;
		int yc;
		int d_x;
		int d_y;
		long vc;
		long vc_1;
		long vc_2;
		double t1;
		double t2;
		
		if(e.is_point() && cross(e.p1(), touch)) {
			res = new Point[1];
			res[0] = e.m_p1;
			return res; 
		}
		
		vc = m_dx * e.m_dy - e.m_dx * m_dy; 
		if(vc == 0) {
			boolean p1_in;
			boolean p2_in;
			int np;
			
			np = 0;
			p1_in = cross(e.m_p1, touch);
			if(p1_in) { ++np; }
			p2_in = cross(e.m_p2, touch);
			if(p2_in) { ++np; }
			if(np == 0) { return null; }
			res = new Point[np];
			np = 0;
			if(p1_in) { res[np++] = new Point(e.m_p1); }
			if(p2_in) { res[np++] = new Point(e.m_p2); }
			return res;
		}
		
		d_x = e.m_p1.x() - m_p1.x();
		d_y = e.m_p1.y() - m_p1.y();
		vc_1 = d_x * m_dy - d_y * e.m_dx;
		vc_2 =  m_dx * d_y - d_x * m_dy;
		vc_2 = -vc_2;
		
		if(vc_1 < 0 || vc_2 < 0 || vc_1 > vc || vc_2 > vc) { return null; }
		if(!touch && (vc_1 == 0 || vc_2 == 0)) { return null; }
		
		t1 = (double)vc_1 / vc; // t1 in [0..1]
		t2 = (double)vc_2 / vc; // t2 in [0..1]
		
		if(t1 > t2) {
			xc = (int)Math.floor(m_p1.x() + m_dx * t1);
			yc = (int)Math.floor(m_p1.y() + m_dy * t1);
		} else {
			xc = (int)Math.floor(e.m_p1.x() + m_dx * t2);
			yc = (int)Math.floor(e.m_p1.y() + m_dy * t2);
		}
		res = new Point[1];
		res[0] = new Point(xc, yc);
		return res;
	}
	
	public boolean is_inside(BBox b, boolean touch) {
		int q1;
		int q2;
		
		q1 = b.query(m_p1);
		q2 = b.query(m_p2);
		return ((q1 > BBox.INTERSECT || (touch && q1 > BBox.NO_INTERSECT)) && (q2 > BBox.INTERSECT || (touch && q2 > BBox.NO_INTERSECT))); 
	}
}
