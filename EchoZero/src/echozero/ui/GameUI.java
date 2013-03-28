package echozero.ui;

import java.awt.Graphics;

import echozero.graphics.EchoGraphicsEngine;
import echozero.util.Program;

public class GameUI {
	
	public class Direction {
		public static final int DIR_UP = 0;
		public static final int DIR_DOWN = 1;
		public static final int DIR_LEFT = 2;
		public static final int DIR_RIGHT = 3;
	}
	
	private EchoGraphicsEngine m_gi;
	private GameUIInput m_input;
	private boolean m_exit;
	private boolean[] m_scroll;
	
	private int m_x;
	private int m_y;
	
	public GameUI(EchoGraphicsEngine gi) {
		m_gi = gi;
		m_input = new GameUIInput(this);
		m_exit = false;
		m_scroll = new boolean[4];
	}
	
	public void set_scroll(int dir, boolean value) {
		m_scroll[dir] = value;
	}
	
	public void render() {
		m_gi.clear_all();
	}
	
	public void loop() {
		long dt;
		
		while(!m_exit) {
			double sc;
			
			dt = -System.nanoTime();
			m_gi.set_current_buffer();
			render();
			m_gi.switch_buffers();
			dt += System.nanoTime();
			
			sc = dt / 1e9 * 100.0;
			if(m_scroll[GameUI.Direction.DIR_UP] && !m_scroll[GameUI.Direction.DIR_DOWN]) 		{ m_y -= sc; }
			if(m_scroll[GameUI.Direction.DIR_DOWN] && !m_scroll[GameUI.Direction.DIR_UP]) 		{ m_y += sc; }		
			if(m_scroll[GameUI.Direction.DIR_LEFT] && !m_scroll[GameUI.Direction.DIR_RIGHT]) 	{ m_x -= sc; }
			if(m_scroll[GameUI.Direction.DIR_RIGHT] && !m_scroll[GameUI.Direction.DIR_LEFT]) 	{ m_x += sc; }
			
			try { Thread.sleep(10); } // 100 FPS
			catch(Exception e) {}
		}
	}
}
