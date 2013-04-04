package echozero.ui;

import echozero.util.Program;

public class GameUIInput extends GameInputAdapter {
	private static final int key_map_size = 8 * 1024;
	private static final int n_keys = 32 + 1;
	
	private static final int KEY_LEFT = 0;
	private static final int KEY_UP = 1;
	private static final int KEY_RIGHT = 2;
	private static final int KEY_DOWN = 3;
	private GameUI m_gu;
	private long[] m_key_time_dn_up;
	private long[] m_key_time_up_dn;
	private int[] m_key_map;
	private boolean[] m_key_state;
	private boolean m_is_repeating;
	

	void init_key_map() {
		m_key_map = new int[key_map_size];
		m_key_map[39 * 2] = 1;
		m_key_map[38 * 2] = 2;
		m_key_map[37 * 2] = 3;
		m_key_map[40 * 2] = 4;
	}
	
	public GameUIInput(GameUI gu) { 
		m_gu = gu;
		init_key_map();
		m_key_state = new boolean[n_keys];
		m_key_time_dn_up = new long[n_keys];
		m_key_time_up_dn = new long[n_keys];
		m_is_repeating = false;
	}
	
	int remap(int keycode, int mod) {
		int key;

		if(keycode * (mod + 1) >= key_map_size) {
			Program.log.log_message(0, String.format("key_code: %d mod: %d > key_map_size", keycode, mod));
			return 0;
		}
		key = m_key_map[keycode * (mod + 1)];
		if(key == 0) {
			Program.log.log_message(0, String.format("key_code: %d mod: %d is not mapped", keycode, mod));
		}
		return key;
	}
	
	void set_scrolling(int key, boolean val) {
		m_gu.scroll(key - 1, val);
	}
	
	public void key_up(long time, int keycode, int mod) {
		int key;

		//Program.log.log_message(0, "key up: " + keycode + " @ " + time);		
		key = remap(keycode, mod);
		m_key_time_dn_up[key] += time;
		m_key_time_up_dn[key] = -time;
		//System.err.println(String.format("key_dn_up(%d) = %d", key, m_key_time_dn_up[key]));
		set_scrolling(key, false);
		if(!m_key_state[key]) { return; } 
		m_key_state[key] = false;
		//System.err.println("key " + key + " is released");			
	}
	
	public void key_dn(long time, int keycode, int mod) {
		int key;

		//Program.log.log_message(0, "key dn: " + keycode + " @ " + time);		
		key = remap(keycode, mod);
		m_key_time_up_dn[key] += time;
		m_key_time_dn_up[key] = -time;			
		//System.err.println(String.format("key_up_dn(%d) = %d", key, m_key_time_up_dn[key]));		
		if(m_key_state[key]) { return; }
		set_scrolling(key, true);
		if(m_key_time_up_dn[key] != 0) {
			m_key_state[key] = true;
			//System.err.println("key " + key + " is pressed");
		} else {
			m_is_repeating = true;
		}
	}
}
