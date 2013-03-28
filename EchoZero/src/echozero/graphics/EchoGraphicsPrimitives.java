package echozero.graphics;

public class EchoGraphicsPrimitives {
	public class Primitive {
		
	}
	
	public class Color extends Primitive {
		private double[] m_color;
		
		public Color(double r, double g, double b, double a) { 
			m_color = new double[4];
			
			m_color[0] = r;
			m_color[1] = g;
			m_color[2] = b;
			m_color[3] = a;
		}
	}
	
	public class Circle extends Primitive {
		
	}
}
