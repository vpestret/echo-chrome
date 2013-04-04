package echozero.game.entity;

import echozero.graphics.EchoGraphicsEngine;
import echozero.math.Vector2;

public class Entity implements EntityInteraction  {
	protected Vector2 m_x;
	protected Vector2 m_v;
	
	public Entity(double x, double y) {
		m_x = new Vector2(x, y);
		m_v = new Vector2(0, 0);
	}
	
	public Entity(double x, double y, double vx, double vy) {
		m_x = new Vector2(x, y);
		m_v = new Vector2(vx, vy);
	}
	
	public void draw(EchoGraphicsEngine ege) {}
	
	public Vector2 get_x() { return m_x; }
	public Vector2 get_v() { return m_v; }

	public void update(double dt) {}
	public boolean is_alive() { return true; }
}
