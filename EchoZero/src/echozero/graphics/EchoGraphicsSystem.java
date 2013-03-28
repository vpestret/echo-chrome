package echozero.graphics;

public class EchoGraphicsSystem {
	private HostGraphics m_hg;
	
	public EchoGraphicsSystem(HostGraphics hg) {
		m_hg = hg;
	}
	
	public void set_current_buffer() {
		m_hg.set_current_buffer();
	}
	
	public void switch_buffers() {
		m_hg.switch_buffers();
	}
	
	public void clear_all() {
		m_hg.clear_all();
	}
	
	public void set_color(double r, double g, double b, double a) { 
		m_hg.set_color(r, g, b, a); 
	}
	
	public void circle(int x, int y, int rad) {
		m_hg.circle(x, y, rad);
	}
	
	public void line(int x1, int y1, int x2, int y2) {
		m_hg.line(x1, y1, x2, y2);
	}
	
	public void rect(int x1, int y1, int x2, int y2) {
		m_hg.rect(x1, y1, x2, y2);
	}

}
