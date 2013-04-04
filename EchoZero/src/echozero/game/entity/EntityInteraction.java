package echozero.game.entity;

import echozero.game.Interaction;
import echozero.graphics.EchoGraphicsEngine;

public interface EntityInteraction extends Interaction {
	public void draw(EchoGraphicsEngine ege);
}
