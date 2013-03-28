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
			case KEY_UP: 	 	m_gu.set_scroll(GameUI.Direction.DIR_UP, false); break;
			case KEY_DOWN:  	m_gu.set_scroll(GameUI.Direction.DIR_DOWN, false); break;
			case KEY_LEFT: 		m_gu.set_scroll(GameUI.Direction.DIR_LEFT, false); break;
			case KEY_RIGHT: 	m_gu.set_scroll(GameUI.Direction.DIR_RIGHT, false); break;
		}
		
		Program.log.log_message(0, "key up: " + keycode + " @ " + time);
	}
	
	public void key_dn(long time, int keycode) {
		switch(keycode) {
			case KEY_UP: 	 	m_gu.set_scroll(GameUI.Direction.DIR_UP, true); break;
			case KEY_DOWN:  	m_gu.set_scroll(GameUI.Direction.DIR_DOWN, true); break;
			case KEY_LEFT: 		m_gu.set_scroll(GameUI.Direction.DIR_LEFT, true); break;
			case KEY_RIGHT: 	m_gu.set_scroll(GameUI.Direction.DIR_RIGHT, true); break;
		}
		
		Program.log.log_message(0, "key dn: " + keycode + " @ " + time);
	}
}
