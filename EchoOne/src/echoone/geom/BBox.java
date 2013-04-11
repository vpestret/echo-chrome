package echoone.geom;

public class BBox {
	public static final int NO_INTERSECT = 0;
	public static final int CORNER_TOUCH = 1;
	public static final int XSIDE_TOUCH = 2;
	public static final int YSIDE_TOUCH = 3;
	public static final int INTERSECT = 4;
	private int m_xl;
	private int m_yb;
	private int m_xr;
	private int m_yt;
	
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
	
	public BBox union(BBox b) {
		if(b.m_xl < m_xl) { m_xl = b.m_xl; }
		if(b.m_yb < m_yb) { m_yb = b.m_yb; }
		if(b.m_xr > m_xr) { m_xr = b.m_xr; }
		if(b.m_yt > m_yt) { m_yt = b.m_yt; }
		return this;
	}
	
	public BBox intersect(BBox b) {
		if(b.m_xl > m_xl) { m_xl = b.m_xl; }
		if(b.m_yb > m_yb) { m_yb = b.m_yb; }
		if(b.m_xr < m_xr) { m_xr = b.m_xr; }
		if(b.m_yt < m_yt) { m_yt = b.m_yt; }
		return this;
	}

	public boolean is_empty() { return(m_xl > m_xr && m_yb > m_yt); }
	public boolean is_point() { return(m_xl == m_xr && m_yb == m_yt); }
	public boolean is_zero_area() { return(m_xl >= m_xr || m_yb >= m_yt); }
	
	public static int intersection_status(BBox a, BBox b) {
		int xl;
		int xr;
		int yb;
		int yt;
		boolean gt_x;
		boolean gt_y;
		boolean eq_x;
		boolean eq_y;
		
		xl = a.m_xl;
		xr = a.m_xr;
		yb = a.m_yb;
		yt = a.m_yt;
		
		if(b.m_xl > xl) { xl = b.m_xl; }
		if(b.m_yb > yb) { yb = b.m_yb; }
		if(b.m_xr < xr) { xr = b.m_xr; }
		if(b.m_yt < yt) { yt = b.m_yt; }
		
		gt_x = (xl > xr);
		gt_y = (yb > yt);
		eq_x = (xl == xr);
		eq_y = (yb == yt);
		
		if(gt_x && gt_y) { return NO_INTERSECT; }
		if(eq_x && eq_y) { return CORNER_TOUCH; }
		if(eq_x && !eq_y) { return XSIDE_TOUCH; }
		if(!eq_x && eq_y) { return YSIDE_TOUCH; }
		return INTERSECT;
	}
	
	public Point get_center() {
		long x;
		long y;
		
		x = (long)m_xl + (long)m_xr;
		y = (long)m_yb + (long)m_yt;
		x /= 2;
		y /= 2;
		return new Point((int)x,(int)y); 
	}
}
