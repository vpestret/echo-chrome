package echozero.host;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

import echozero.ui.GameInput;

class AWTMouseListener extends MouseAdapter {
	private AWTInput m_inp;
	
	public AWTMouseListener(AWTInput inp) { m_inp = inp; }

}

class AWTKeyListener extends KeyAdapter {
	private AWTInput m_inp;
	
	public AWTKeyListener(AWTInput inp) { m_inp = inp; }
}


public class AWTInput {
	private GameInput m_ginp;
	
	public AWTInput(Frame f, GameInput ginp) {
		m_ginp = ginp;
		f.addMouseListener(new AWTMouseListener(this));
		f.addKeyListener(new AWTKeyListener(this));
	}
}
