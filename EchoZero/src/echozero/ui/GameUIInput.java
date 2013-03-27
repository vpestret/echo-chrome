package echozero.ui;

import echozero.util.Program;

public class GameUIInput extends GameInputAdapter {
	private static final int n_keys = 8;
	private GameUI m_gu;

	public GameUIInput(GameUI gu) { 
		m_gu = gu;
	}
	
	public void key_up(long time, int keycode) {
		Program.log.log_message(0, "key up: " + keycode + " @ " + time);
	}
	
	public void key_dn(long time, int keycode) {
		Program.log.log_message(0, "key dn: " + keycode + " @ " + time);
	}
}
