package echozero.host;

import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import echozero.graphics.host.HostGraphics;
import echozero.graphics.host.HostGraphicsCapabilities;
import echozero.util.Program;

class AWTWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) { 
		Window w = e.getWindow(); 
		w.setVisible(false); 
		w.dispose(); 
		System.exit(0); 
	} 
}

public class AWTImage implements HostGraphics {
	private Frame m_frame;
	private Rectangle m_bounds;
	private BufferStrategy m_buffer;
	private Graphics m_current;
	
	public AWTImage(boolean windowed, int w, int h) throws IOException {
        GraphicsEnvironment env;
        GraphicsConfiguration gc;
        
        try {
        	if(!windowed) {
        		GraphicsDevice gd;
        		
        		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        		gd = env.getDefaultScreenDevice();
        		gc = gd.getDefaultConfiguration();
        		m_frame = new Frame(gc);
        		m_frame.setUndecorated(true);
        		gd.setFullScreenWindow(m_frame);
        	} else {
        		m_frame = new Frame("-)cho Chrom(-");
        		m_frame.setSize(w, h);
        		m_frame.setVisible(true);
        		m_frame.addWindowListener(new AWTWindowListener());
        	}
        	
            if(!windowed) { m_frame.setIgnoreRepaint(true); }
            
            m_frame.createBufferStrategy(2);
            m_buffer = m_frame.getBufferStrategy();
            BufferCapabilities cap = m_buffer.getCapabilities();
            m_bounds = m_frame.getBounds();
            
        	Program.log.log_message(0, "device acquired: bounds: " + 
        			m_bounds.getMinX() + "," + m_bounds.getMinY() + " x " +
        			m_bounds.getMaxX() + "," + m_bounds.getMaxY() 
        	);
        	Program.log.log_message(0, "page flipping: " + (cap.isPageFlipping() ? "true" : "false"));
        }

        catch(Exception e) {
        	System.err.println(e.getMessage());
        	throw new IOException();
        }
    }
	
    public AWTImage() throws IOException {
    	this(true, 1280, 1024);
    }
    
    public HostGraphicsCapabilities get_caps() {
    	return new HostGraphicsCapabilities(m_bounds.width, m_bounds.height);
    }
                
	public void set_current_buffer() { 
		m_current = m_buffer.getDrawGraphics();
	}
	
	public void switch_buffers() {
		m_buffer.show();
		m_current.dispose();		
	}

	public void clear_all() {
		m_current.setColor(Color.BLACK);
        m_current.fillRect(0, 0, m_bounds.width, m_bounds.height);
	}
	
	/* Color with alpha is very slow on AWT */
	public void set_color(double r, double g, double b, double a) {
		m_current.setPaintMode();
		m_current.setColor(new Color((float)r, (float)g, (float)b));
	}
	
	public void xor_mode(double r, double g, double b, double a) {
		m_current.setXORMode(new Color((float)r, (float)g, (float)b));
	}
	
	public void line(int x1, int y1, int x2, int y2) {
		m_current.drawLine(x1, y1, x2, y2);
	}
	
	public void rect(int x1, int y1, int x2, int y2) {
		m_current.drawRect(x1,  x1, x2 - x1, y2 - y1);
	}
	
	public void text(int x1, int y1, String s) {
		m_current.drawString(s, x1, y1);
	}
	
	public void poly(int np, int x[], int y[], boolean fill) {
		if(fill) {
			m_current.fillPolygon(x, y, np);
		} else {
			m_current.drawPolygon(x, y, np);			
		}
	}
	
	public Frame get_frame() { return m_frame; }
}
