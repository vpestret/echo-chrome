package echozero.ui;

import echozero.graphics.EchoGraphicsEngine;
import echozero.graphics.primitives.Color;
import echozero.graphics.primitives.Edge;
import echozero.graphics.primitives.PrimitiveList;

public class Grid {
	private PrimitiveList m_pl;
	
	public Grid(double dx, double dy) {
		double x;
		double y;
		
		m_pl = new PrimitiveList();
		m_pl.add(new Color(1, 1, 0, 0));
		
		for(x = 0; x < 1 + dx - dx / 10; x += dx) { m_pl.add(new Edge(x, 0, x, 1)); }
		for(y = 0; y < 1 + dy - dy / 10; y += dy) { m_pl.add(new Edge(0, y, 1, y)); }
	}
	
	public void draw(EchoGraphicsEngine ege) {
		ege.draw_prim_list(m_pl); 
	}
}
