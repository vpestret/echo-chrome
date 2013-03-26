package echozero.host;

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

import echozero.ui.GameImage;
import echozero.util.Program;

class AWTWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) { 
		Window w = e.getWindow(); 
		w.setVisible(false); 
		w.dispose(); 
		System.exit(0); 
	} 
}

public class AWTImage implements GameImage {
	private Frame m_frame;
	private Rectangle m_bounds;
	private BufferStrategy m_buffer;
	private Graphics m_current;
	
	public AWTImage(boolean windowed) throws IOException {
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
        		m_frame.setSize(1024, 600);
        		m_frame.setVisible(true);
        		m_frame.addWindowListener(new AWTWindowListener());
        	}
        	
            m_frame.setIgnoreRepaint(true);            
            m_frame.createBufferStrategy(2);
            m_buffer = m_frame.getBufferStrategy();
            m_bounds = m_frame.getBounds();
            
        	Program.log.log_message(0, "device acquired: bounds: " + 
        			m_bounds.getMinX() + "," + m_bounds.getMinY() + " x " +
        			m_bounds.getMaxX() + "," + m_bounds.getMaxY() 
        	);
        }

        catch(Exception e) {
        	System.err.println(e.getMessage());
        	throw new IOException();
        }
    }
	
    public AWTImage() throws IOException {
    	this(true);
    }
                
	public void set_current_buffer() {
		m_current = m_buffer.getDrawGraphics();
	}
	
	public void switch_buffers() {
        m_buffer.show();
		m_current.dispose();
	}

	public void clear_all() {
        m_current.setColor(Color.darkGray);
        m_current.fillRect(0, 0, m_bounds.width, m_bounds.height);
	}
	
	public Frame get_frame() { return m_frame; }
}
