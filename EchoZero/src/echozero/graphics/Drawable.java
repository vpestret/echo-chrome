package echozero.graphics;

import echozero.math.Matrix3;

interface Drawable {
	public void draw(Matrix3 tr, HostGraphics hg);
}