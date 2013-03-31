package echozero.game;

import echozero.game.objects.Missile;
import echozero.graphics.EchoGraphicsEngine;
import echozero.util.DList;

public class GameState {
	private DList<Entity> m_ent;
	
	public GameState() {
		m_ent = new DList<Entity>();
		m_ent.push_back(new Missile(0.5, 0.5));
	}
	
	public void draw(EchoGraphicsEngine ege) {
		for(m_ent.rw(); !m_ent.is_end(); m_ent.next()) { m_ent.get().draw(ege); }
	}
	
	void update_time(double sec) {
		
	}
}
