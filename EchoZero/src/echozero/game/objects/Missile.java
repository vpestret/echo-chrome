package echozero.game.objects;

import echozero.game.Entity;
import echozero.graphics.Color;
import echozero.graphics.EchoGraphicsEngine;
import echozero.graphics.PrimitiveList;
import echozero.graphics.RegPoly;
import echozero.math.Matrix3;

public class Missile extends Entity {
	PrimitiveList m_pr;
	
	public Missile(double x, double y) {
		super(x, y);
		m_pr = new PrimitiveList();
		m_pr.add(new Color(1, 0, 0, 0));
		m_pr.add(new RegPoly(3, 0.1, true));
	}
	
	
	public void draw(EchoGraphicsEngine ege) {
		ege.push_matrix();
		ege.translate(m_x.get_x()[0], m_x.get_x()[1]);
		ege.draw_prim_list(m_pr);
		ege.pop_matrix();
	}
}
