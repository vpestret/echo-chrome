package echozero.host;

import java.awt.Frame;

import echozero.ui.GameInput;



public class AWTInput {
	private GameInput m_ginp;
	
	public AWTInput(Frame f, GameInput ginp) {

		AWTMouseListener ml;
		
		m_ginp = ginp;
		
		ml = new AWTMouseListener(this);
		
		f.addKeyListener(new AWTKeyListener(this, f));
		
		f.addMouseListener(ml);
		f.addMouseMotionListener(ml);
		f.addMouseWheelListener(ml);
	}
}
