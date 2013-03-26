package echozero.host;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import echozero.util.Program;

public class AWTMouseListener extends MouseAdapter {
		private AWTInput m_inp;
	
		public AWTMouseListener(AWTInput inp) { 
			m_inp = inp;
			Program.log.log_message(0, "mouse_listener is constructed with " + m_inp.toString() + " = " + this.toString());
		}
		
		public void mouseClicked(MouseEvent e) {
			int x;
			int y;
			int c;
			int b;

			x = e.getX();
			y = e.getY();
			b = e.getButton();
			c = e.getClickCount();
			
			Program.log.log_message(0, "mouse button " + b + " is clicked " + c + " times at " + x + ", " + y);			
		}
		
		public void mouseMoved(MouseEvent e) {
			int x;
			int y;

			x = e.getX();
			y = e.getY();
			
			Program.log.log_message(0, "mouse is moved at " + x + ", " + y);			
		}
		
		public void mousePressed(MouseEvent e) {
			int x;
			int y;
			int b;
			
			x = e.getX();
			y = e.getY();
			b = e.getButton();
			//but = (b == e.BUTTON1) ? "1" : (b == e.BUTTON2) ? "2" : (b == e.BUTTON3) ? "3" : "UNKNOWN";
			Program.log.log_message(0, "mouse button " + b + " is pressed at " + x + ", " + y);
		}

		public void mouseReleased(MouseEvent e) {
			int x;
			int y;
			int b;
			
			x = e.getX();
			y = e.getY();
			b = e.getButton();
			//but = (b == e.BUTTON1) ? "1" : (b == e.BUTTON2) ? "2" : (b == e.BUTTON3) ? "3" : "UNKNOWN";
			Program.log.log_message(0, "button " + b + " is pressed at " + x + ", " + y);	
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			int rot;
			
			rot = e.getWheelRotation();
			Program.log.log_message(0, "wheel: " + rot);
		}
}
