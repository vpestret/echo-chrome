package echozero.ui;

public interface GameInput {
	public void mouse_moved_to(long time, int x, int y);
	public void mouse_key_up(long time, int x, int y, int b);
	public void mouse_key_dn(long time, int x, int y, int b);
	public void mouse_wheel(long time, int x, int y, int v);
	public void key_up(long time, int keycode, int modifier);
	public void key_dn(long time, int keycode, int modifier);
}
