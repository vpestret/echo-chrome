package echozero.graphics;

public interface HostGraphics {
	public HostGraphicsCapabilities get_caps();
	
	public void set_current_buffer();
	public void switch_buffers();
	
	public void clear_all();
	
	public void set_color(double r, double g, double b, double a);
	public void xor_mode(double r, double g, double b, double a);
	
	public void line(int x1, int y1, int x2, int y2);
	public void rect(int x1, int y1, int x2, int y2);
	public void poly(int np, int x[], int y[], boolean fill);
	
	public void text(int x1, int y1, String s);
}
