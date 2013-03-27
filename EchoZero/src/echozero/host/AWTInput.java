package echozero.host;

import java.awt.Frame;

import echozero.ui.GameInput;



public class AWTInput {
	private GameInput m_ginp;
	
	public AWTInput(Frame f, GameInput ginp) {
		m_ginp = ginp;
		
		f.addKeyListener(new AWTKeyListener(m_ginp));
		f.addMouseListener(new AWTMouseListener(m_ginp));
	}
}
