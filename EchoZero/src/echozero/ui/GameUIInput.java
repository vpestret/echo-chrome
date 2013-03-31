package echozero.ui;

import echozero.util.Program;

public class GameUIInput extends GameInputAdapter {
	private static final int n_keys = 8;
	private static final int KEY_LEFT = 37;
	private static final int KEY_UP = 38;
	private static final int KEY_RIGHT = 39;
	private static final int KEY_DOWN = 40;
	private GameUI m_gu;

	public GameUIInput(GameUI gu) { 
		m_gu = gu;
	}
	
	public void key_up(long time, int keycode) {
		switch(keycode) {
		}
		Program.log.log_message(0, "key up: " + keycode + " @ " + time);
	}
	
	public void key_dn(long time, int keycode) {
		switch(keycode) {
		}
		Program.log.log_message(0, "key dn: " + keycode + " @ " + time);
	}
}
