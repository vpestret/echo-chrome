package echozero.host;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import echozero.util.Program;

public class AWTKeyListener extends KeyAdapter {
	private AWTInput m_inp;
	private Frame m_f;
	
	public AWTKeyListener(AWTInput inp, Frame f) { 
		m_inp = inp;
		m_f = f;
		Program.log.log_message(0, "key_listener is constructed with " + m_inp.toString());		
	}
	
	public void keyPressed(KeyEvent e) {
		int code;
		int i;
		
		code = e.getKeyCode();
		Program.log.log_message(0, "key code " + code);
		
		for(i = 0; i < m_f.getMouseListeners().length; ++i) {
			Program.log.log_message(0, "mouse listener: " + m_f.getMouseListeners()[i]);
		}
		
		for(i = 0; i < m_f.getMouseWheelListeners().length; ++i) {
			Program.log.log_message(0, "mouse wheel listener: " + m_f.getMouseWheelListeners()[i]);
		}

		for(i = 0; i < m_f.getMouseMotionListeners().length; ++i) {
			Program.log.log_message(0, "mouse motion listener: " + m_f.getMouseMotionListeners()[i]);
		}
		
	}
}
