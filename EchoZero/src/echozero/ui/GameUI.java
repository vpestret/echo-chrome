package echozero.ui;

import java.awt.Graphics;

import echozero.game.GameState;
import echozero.graphics.EchoGraphicsEngine;
import echozero.util.Program;

public class GameUI {
	public class Direction {
		public static final int DIR_UP = 0;
		public static final int DIR_DOWN = 1;
		public static final int DIR_LEFT = 2;
		public static final int DIR_RIGHT = 3;
	}
	
	// global state holders 
	private EchoGraphicsEngine m_gi;
	private GameState m_gs;
	private GameUIInput m_input;
	
	// ui
	private boolean m_exit;
	private Grid m_grid;
	
	public GameUI(EchoGraphicsEngine gi, GameState gs) {
		m_gs = gs;
		m_gi = gi;
		m_input = new GameUIInput(this);
		m_exit = false;
		m_grid = new Grid(0.05, 0.05);
	}
	
	public void render() {
		m_gi.clear_all();
		m_grid.draw(m_gi);
	}
	
	public void loop() {
		double fps;
		
		fps = 0.0;
		while(!m_exit) {
			long dt_ns;
			double sc;
			double dt;
			
			dt_ns = -System.nanoTime();
			
			m_gi.set_current_buffer();
			render();
			m_gi.draw_fps(fps);			
			m_gi.switch_buffers();
			
			dt_ns += System.nanoTime();
			dt = dt_ns * 1.0 / 1e6;
			fps = 1000 / dt;
			
			/* FPS limiter */
			if(dt < 10) {
				
				try { Thread.sleep((long)(10 - dt)); } // 100 FPS
				catch(Exception e) {}
			}
		}
	}
}
