package echozero.host;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import echozero.ui.GameInput;

public class AWTKeyListener extends KeyAdapter {
	private GameInput m_inp;

	public AWTKeyListener(GameInput inp) { m_inp = inp; }
	
	public void keyReleased(KeyEvent e) {
		int code;
		
		code = e.getKeyCode();
		m_inp.key_up(e.getWhen(), code, e.getKeyLocation());
	}
	
	public void keyPressed(KeyEvent e) {
		int code;
		
		code = e.getKeyCode();
		m_inp.key_dn(e.getWhen(), code, e.getKeyLocation());
	}
}
