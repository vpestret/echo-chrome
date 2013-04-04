package echozero.game.objects;

import echozero.game.entity.Entity;
import echozero.graphics.EchoGraphicsEngine;
import echozero.graphics.primitives.Color;
import echozero.graphics.primitives.PrimitiveList;
import echozero.graphics.primitives.RegPoly;
import echozero.graphics.primitives.XORMode;


public class Target extends Entity {
	private static final double R = 0.01;
	PrimitiveList m_pr;
	double m_angle;
	
	public Target(double x, double y) {
		super(x, y);
		regen_prim(R);
		m_angle = 0;
	}
	
	public void regen_prim(double r) {
		m_pr = new PrimitiveList();
		m_pr.add(new Color(1, 1, 0, 0));
		m_pr.add(new RegPoly(5, r, true));
		m_pr.add(new XORMode(0, 0, 0, 0));
		m_pr.add(new RegPoly(5, 3 * r / 4, true));
	}
	
	public void update(double dt) {
		m_angle -= dt * 2 * Math.PI / 300;
		while(m_angle > 2 * Math.PI) { m_angle -= 2 * Math.PI; }
	}
	
	public void draw(EchoGraphicsEngine ege) {
		ege.push_matrix();
		ege.rotate(m_angle);		
		ege.translate(m_x.get_x()[0], m_x.get_x()[1]);
		ege.draw_prim_list(m_pr);
		ege.pop_matrix();
	}
}