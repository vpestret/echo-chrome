package echozero.host;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import echozero.ui.GameInput;

public class AWTMouseListener extends MouseAdapter {
		private GameInput m_inp;
	
		public AWTMouseListener(GameInput inp) { 
			m_inp = inp;
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			int rot;
			int x;
			int y;

			x = e.getX();
			y = e.getY();
			rot = e.getWheelRotation();
			m_inp.mouse_wheel(System.nanoTime(), x, y, rot);
		}
		
		public void mouseMoved(MouseEvent e) {
			int x;
			int y;

			x = e.getX();
			y = e.getY();
			m_inp.mouse_moved_to(System.nanoTime(), x, y);
		}
		
		public void mousePressed(MouseEvent e) {
			int x;
			int y;
			int b;
			
			x = e.getX();
			y = e.getY();
			b = e.getButton();
			m_inp.mouse_key_dn(System.nanoTime(), x, y, b);
		}

		public void mouseReleased(MouseEvent e) {
			int x;
			int y;
			int b;
			
			x = e.getX();
			y = e.getY();
			b = e.getButton();
			m_inp.mouse_key_up(System.nanoTime(), x, y, b);
		}
		
}
