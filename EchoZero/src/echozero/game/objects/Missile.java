package echozero.game.objects;

import echozero.game.entity.Entity;
import echozero.graphics.EchoGraphicsEngine;
import echozero.graphics.primitives.Color;
import echozero.graphics.primitives.PrimitiveList;
import echozero.graphics.primitives.RegPoly;
import echozero.graphics.primitives.Triangle;
import echozero.graphics.primitives.XORMode;
import echozero.math.Matrix3;

public class Missile extends Entity {
	private static final double R = 0.01;	
	private PrimitiveList m_pr;
	private double m_angle;
	private double m_scale;
	
	public Missile(double x, double y) {
		super(x, y);
		m_angle = 0;
		m_scale = 1;
		regen_prim(R);
	}
	
	void regen_prim(double r) {
		m_pr = new PrimitiveList();
		m_pr.add(new Color(1, 0, 0, 0));
		m_pr.add(new RegPoly(3, r, true));
		m_pr.add(new XORMode(0, 0, 0, 0));
		m_pr.add(new Triangle(
				r * Math.cos(2 * Math.PI / 3), r * Math.sin(2 * Math.PI / 3),
				r * Math.cos(4 * Math.PI / 3), r * Math.sin(4 * Math.PI / 3),
				r * 0.5,  0,
				true
				));
	}
	
	public void update(double dt) {
		m_angle += dt * 2 * Math.PI / 500;
		while(m_angle > 2 * Math.PI) { m_angle -= 2 * Math.PI; }
	}
	
	public void draw(EchoGraphicsEngine ege) {
		ege.push_matrix();
		ege.translate(m_x.get_x()[0], m_x.get_x()[1]);		
		ege.rotate(m_angle);
		ege.scale(m_scale, m_scale);
		ege.draw_prim_list(m_pr);
		ege.pop_matrix();
	}
}
