package echozero.ui;

import java.awt.Graphics;

import echozero.util.Program;

public class GameUI implements GameInput {
	private GameImage m_gi;
	
	public GameUI(GameImage gi) {
		m_gi = gi;
	}
	
	public void loop() {
		int t;
		
		t = 0;
		for (float lag = 2000.0f; t < 20 && lag > 0.00000006f; lag = lag / 1.33f, ++t) {
			m_gi.set_current_buffer();
			m_gi.clear_all();
			m_gi.switch_buffers();
			
                try {
                	Program.log.log_message(0, "thread sleeps for " + (int)lag);
                    Thread.sleep((int)lag);
                } catch (InterruptedException e) {}
              
        }
	}
}
