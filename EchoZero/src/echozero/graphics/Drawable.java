package echozero.graphics;

import echozero.graphics.host.HostGraphics;
import echozero.math.Matrix3;

public interface Drawable {
	public void draw(Matrix3 tr, HostGraphics hg);
}