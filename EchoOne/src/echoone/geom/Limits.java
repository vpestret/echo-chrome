package echoone.geom;

public class Limits {
	private static int m_range = 1 << 29;
	public static void set_range(int range) { m_range = range; }
	public static int get_range() { return m_range; }
}
