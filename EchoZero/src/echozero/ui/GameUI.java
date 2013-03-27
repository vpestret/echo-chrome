package echozero.ui;

import java.awt.Graphics;

import echozero.util.Program;

public class GameUI {
	private GameImage m_gi;
	private GameUIInput m_input;
	private boolean m_exit;
	
	public GameUI(GameImage gi) {
		m_gi = gi;
		m_input = new GameUIInput(this);
		m_exit = false;
	}
	
	public void render() {
		m_gi.clear_all();
		m_gi.set_color(1, 0, 0, 0.5);
		m_gi.circle(0, 0, 100);
		m_gi.set_color(0, 0, 1, 0.5);
		m_gi.circle(0, 0, 50);
	}
	
	public void loop() {
		long dt;
		
		while(!m_exit) {
			dt = -System.nanoTime();
			m_gi.set_current_buffer();
			render();
			m_gi.switch_buffers();
			dt += System.nanoTime();
			try { Thread.sleep(10); } // sleep for 10 ms
			catch (InterruptedException e) {}
		}
	}
}
