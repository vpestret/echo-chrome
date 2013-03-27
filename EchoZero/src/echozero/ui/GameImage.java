package echozero.ui;

public interface GameImage {
	public void set_current_buffer();
	public void switch_buffers();
	
	public void clear_all();
	
	public void set_color(double r, double g, double b, double a);
	public void circle(int x, int y, int rad);
}
