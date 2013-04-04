package echozero.game;

import echozero.game.entity.Entity;
import echozero.game.objects.Missile;
import echozero.game.objects.Target;
import echozero.graphics.EchoGraphicsEngine;
import echozero.util.DList;

public class GameState {
	private DList<Entity> m_ent;
	
	public GameState() {
		m_ent = new DList<Entity>();
		m_ent.push_back(new Missile(0.5, 0.5));
		m_ent.push_back(new Target(0.3, 0.3));
	}
	
	public void draw(EchoGraphicsEngine ege) {
		for(m_ent.rw(); !m_ent.is_end(); m_ent.next()) { m_ent.get().draw(ege); }
	}
	
	public void update_time(double msec) {
		for(m_ent.rw(); !m_ent.is_end(); m_ent.next()) { 
			m_ent.get().update(msec); 
		}
	}
}
