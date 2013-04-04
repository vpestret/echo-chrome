package echozero.ui;

import java.awt.Graphics;

import echozero.game.GameState;
import echozero.graphics.EchoGraphicsEngine;
import echozero.math.Vector2;
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
	
	private double m_view_x;
	private double m_view_y;
	private double m_view_scale;
	private double m_view_angle;
	private int m_reverse_y;
	
	// ui
	private boolean m_exit;
	private Grid m_grid;
	private boolean[] m_scroll;
	
	public GameUI(EchoGraphicsEngine gi, GameState gs) {
		m_gs = gs;
		m_gi = gi;
		m_input = new GameUIInput(this);
		m_exit = false;
		m_grid = new Grid(0.1, 0.1);
		m_view_scale = 1;
		m_view_angle = 0;
		m_view_x = 0;
		m_view_y = 0;
		m_reverse_y = -1;
		m_scroll = new boolean[4];
	}
	
	public void render() {
		m_gi.clear_all();
		m_gi.push_matrix();
		m_gi.scale(m_view_scale, m_view_scale);
		m_gi.rotate(m_view_angle);
		m_gi.translate(m_view_x, m_view_y);
		m_grid.draw(m_gi);
		m_gs.draw(m_gi);
		m_gi.pop_matrix();
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
			
			m_gs.update_time(dt);
			
			if(m_scroll[0]) { m_view_x += dt * 0.001 / m_view_scale; }
			if(m_scroll[1]) { m_view_y -= m_reverse_y * dt * 0.001 / m_view_scale; }
			if(m_scroll[2]) { m_view_x -= dt * 0.001 / m_view_scale; }
			if(m_scroll[3]) { m_view_y += m_reverse_y * dt * 0.001 / m_view_scale; }
			
			/* FPS limiter */
			if(dt < 10) {
				
				try { Thread.sleep((long)(10 - dt)); } // 100 FPS
				catch(Exception e) {}
			}
		}
	}/* loop */
	
	public void scroll(int dir, boolean value) { m_scroll[dir] = value; }

	Vector2 remap_to_world(int x, int y) {
		return new Vector2(0, 0);
	}
	
	public void zoom(int x, int y, int val) {
		if(val > 0) { m_view_scale += 0.1; }
		else if(val < 0){ m_view_scale -= 0.1; }
	}
}
