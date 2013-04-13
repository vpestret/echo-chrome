package echoone.geom;

public class BBox {
	public static final int NO_INTERSECT 			= 0; /* indicates no interaction with given object */
	public static final int CORNER_TOUCH 			= 1; /* indicates corner touch with another object */
	public static final int XSIDE_TOUCH_LEFT 		= 2; /* indicates xl-side touch with another object */
	public static final int XSIDE_TOUCH_RIGHT 		= 3; /* indicates xr-side touch with another object */
	public static final int YSIDE_TOUCH_BOTTOM 		= 4; /* indicates yb-side touch with another object */
	public static final int YSIDE_TOUCH_TOP			= 5; /* indicates yt-side touch with another object */
	public static final int INTERSECT 				= 6; /* indicates that objects intersect */
	public static final int INSIDE 					= 7; /* indicates that object is inside */
	private int m_xl;
	private int m_yb;
	private int m_xr;
	private int m_yt;

	public BBox(Point p) {
		m_xl = m_xr = p.x();
		m_yb = m_yt = p.y();
	}
	
	public BBox(int xl, int yb, int xr, int yt) {
		m_xl = xl;
		m_yb = yb;
		m_xr = xr;
		m_yt = yt;
	}
	
	public BBox(BBox b) {
		m_xl = b.m_xl;
		m_yb = b.m_yb;
		m_xr = b.m_xr;
		m_yt = b.m_yt;
	}
	
	public int get_xl() { return m_xl; }
	public int get_xr() { return m_xr; }
	public int get_yb() { return m_yb; }
	public int get_yt() { return m_yt; }
	
	public Point get_lb() { return new Point(m_xl, m_yb); }
	public Point get_rt() { return new Point(m_xr, m_yt); }
	
	public BBox extend(BBox b) {
		if(b.m_xl < m_xl) { m_xl = b.m_xl; }
		if(b.m_yb < m_yb) { m_yb = b.m_yb; }
		if(b.m_xr > m_xr) { m_xr = b.m_xr; }
		if(b.m_yt > m_yt) { m_yt = b.m_yt; }
		return this;
	}
	
	public BBox extend(Point p) {
		if(p.x() < m_xl) { m_xl = p.x(); }
		if(p.y() < m_yb) { m_yb = p.y(); }
		if(p.x() > m_xr) { m_xr = p.x(); }
		if(p.y() > m_yt) { m_yt = p.y(); }
		return this;
	}
	
	
	public BBox intersect(BBox b) {
		if(b.m_xl > m_xl) { m_xl = b.m_xl; }
		if(b.m_yb > m_yb) { m_yb = b.m_yb; }
		if(b.m_xr < m_xr) { m_xr = b.m_xr; }
		if(b.m_yt < m_yt) { m_yt = b.m_yt; }
		return this;
	}
	
	public boolean is_equal(BBox b) { return (m_xl == b.m_xl && m_xr == b.m_xr && m_yb == b.m_yb && m_yt == b.m_yt); }

	public boolean is_empty() { return(m_xl > m_xr && m_yb > m_yt); }
	public boolean is_point() { return(m_xl == m_xr && m_yb == m_yt); }
	public boolean is_zero_area() { return(m_xl >= m_xr || m_yb >= m_yt); }
	
	public int query(Point p) {
		if(p.x() < m_xl || p.x() > m_xr || p.y() < m_yb || p.y() > m_yt) { return NO_INTERSECT; }
		if(p.x() == m_xl || p.x() == m_xr) {
			if(p.y() == m_yb || p.y() == m_yt) { return CORNER_TOUCH; }
			if(p.x() == m_xl) { return XSIDE_TOUCH_LEFT; }
			return XSIDE_TOUCH_RIGHT;
		}
		if(p.y() == m_yb || p.y() == m_yt) {
			if(p.x() == m_xl || p.x() == m_xr) { return CORNER_TOUCH; }
			if(p.y() == m_yb) { return YSIDE_TOUCH_BOTTOM; }
			return YSIDE_TOUCH_TOP;
		}
		return INSIDE;
	}
	
	public int intersection_status(BBox b) {
		int xl;
		int xr;
		int yb;
		int yt;
		boolean gt_x;
		boolean gt_y;
		boolean eq_x;
		boolean eq_y;
		
		xl = m_xl;
		xr = m_xr;
		yb = m_yb;
		yt = m_yt;
		
		if(b.m_xl > xl) { xl = b.m_xl; }
		if(b.m_yb > yb) { yb = b.m_yb; }
		if(b.m_xr < xr) { xr = b.m_xr; }
		if(b.m_yt < yt) { yt = b.m_yt; }

		if(xl == b.m_xl && xr == b.m_xr && yb == b.m_yb && yt == b.m_yt) { return INSIDE; }
		
		gt_x = (xl > xr);
		gt_y = (yb > yt);
		eq_x = (xl == xr);
		eq_y = (yb == yt);
		
		if(gt_x && gt_y) { return NO_INTERSECT; }
		if(eq_x && eq_y) { return CORNER_TOUCH; }
		if(eq_x && !eq_y) {
			if(m_xr == xl) { return XSIDE_TOUCH_RIGHT; }
			return XSIDE_TOUCH_LEFT; 
		}
		if(!eq_x && eq_y) {
			if(m_yb == yb) { return YSIDE_TOUCH_BOTTOM; }
			return YSIDE_TOUCH_TOP; 
		}
		return INTERSECT;
	}
	
	public Point get_center() {
		int x;
		int y;
		
		x = m_xl + m_xr;
		y = m_yb + m_yt;
		x /= 2;
		y /= 2;
		return new Point(x, y); 
	}
}
