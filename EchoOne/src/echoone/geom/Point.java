package echoone.geom;

public class Point {
	private final int[] m_x;
	public Point() { m_x = new int[2]; }
	public Point(int x, int y) {
		this();
		m_x[0] = x; m_x[1] = y;
	}
}
