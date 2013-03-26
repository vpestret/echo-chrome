package echozero.ui;

import java.awt.Graphics;

import echozero.util.Program;

public class GameUI implements GameInput {
	private GameImage m_gi;
	
	public GameUI(GameImage gi) {
		m_gi = gi;
	}
	
	public void loop() {
		m_gi.set_current_buffer();
		m_gi.clear_all();
		m_gi.switch_buffers();
	}
}
