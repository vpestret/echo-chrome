package echozero.graphics.host;

public class HostGraphicsCapabilities {
	private final int m_w;
	private final int m_h;
	
	public HostGraphicsCapabilities(int w, int h) { m_w = w; m_h = h; }
	public int get_width() { return m_w; }
	public int get_height() { return m_h; }
}
