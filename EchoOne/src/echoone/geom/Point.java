package echoone.geom;

public class Point {
	private final int m_x;
	private final int m_y;
	private final long m_r2;
	
	public Point() { m_r2 = m_x = m_y = 0; }
	
	public Point(int x, int y) {
		m_x = x; m_y = y;
		m_r2 = (long)x * x + (long)y * y;
	}
	
	public Point(Point p) {
		m_x = p.m_x;
		m_y = p.m_y;
		m_r2 = p.r2();
	}
	
	public int x() { return m_x; }
	public int y() { return m_y; }
	public long r2() { return m_r2; }
	
	public int compare(Point p) {
		if(p.m_x == m_x && p.m_y == m_y) { return 0; }
		if(m_r2 < p.m_r2) { return -1; }
		return 1;
	}
}
