package echozero.graphics;

public interface HostGraphics {
	public void set_current_buffer();
	public void switch_buffers();
	
	public void clear_all();
	
	public void set_color(double r, double g, double b, double a);
	public void circle(int x, int y, int rad);
	public void line(int x1, int y1, int x2, int y2);
	public void rect(int x1, int y1, int x2, int y2);
}
