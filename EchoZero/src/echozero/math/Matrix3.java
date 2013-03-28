package echozero.math;

public class Matrix3 {
	private double[] m_a;
	
	public Matrix3() { m_a = new double[9]; }

	public Matrix3(Matrix3 m) {
		this();
		int i;
		
		for(i = 0; i < 9; ++i) { m_a[i] = m.m_a[i]; }
	}
	
	public void load_identity() {
		m_a[0] = 1; m_a[1] = 0; m_a[2] = 0;
		m_a[3] = 0; m_a[4] = 1; m_a[5] = 0;
		m_a[6] = 0; m_a[7] = 0; m_a[8] = 1;
	}
	
	public void scale(double s) {
		int i;
		
		for(i = 0; i < 9; ++i) { m_a[i] *= s; }
	}
	
	public void translate_rotate(double alpha, double dx, double dy) {
		double cs;
		double sn;
		
		cs = Math.cos(alpha);
		sn = Math.sin(alpha);
		m_a[0] = cs; m_a[1] = -sn; m_a[2] = -dx;
		m_a[3] = sn; m_a[4] = cs; m_a[5] = -dy;
		m_a[6] = 0; m_a[7] = 0; m_a[8] = 1;
	}
	
	public void translate_rotate_scale(double scale, double alpha, double dx, double dy) {
		double cs;
		double sn;
		int i;
		
		cs = Math.cos(alpha);
		sn = Math.sin(alpha);
		m_a[0] = cs; m_a[1] = -sn; m_a[2] = -dx;
		m_a[3] = sn; m_a[4] = cs; m_a[5] = -dy;
		m_a[6] = 0; m_a[7] = 0; m_a[8] = 1;
		for(i = 0; i < 9; ++i) { m_a[i] *= scale; }
	}
	
	
	public void translate(double dx, double dy) {
		m_a[0] = 1; m_a[1] = 0; m_a[2] = -dx;
		m_a[3] = 0; m_a[4] = 1; m_a[5] = -dy;
		m_a[6] = 0; m_a[7] = 0; m_a[8] = 1;
	}
	
	public void rotate(double alpha) {
		double cs;
		double sn;
		
		cs = Math.cos(alpha);
		sn = Math.sin(alpha);
		m_a[0] = cs; m_a[1] = -sn; m_a[2] = 0;
		m_a[3] = sn; m_a[4] = cs; m_a[5] = 0;
		m_a[6] = 0; m_a[7] = 0; m_a[8] = 1;
	}

	private void multiply(double[] a, double[] b, double[] c) {
		c[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
		c[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
		c[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];
		
		c[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
		c[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
		c[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];
		
		c[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
		c[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
		c[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];
	}
	
	
	public void multiply_left(Matrix3 m) {
		double[] c;
		
		c = new double[9];
		multiply(m.m_a, m_a, c);
		m_a = c;
	}
	
	public void multiply_right(Matrix3 m) {
		double[] c;
		
		c = new double[9];
		multiply(m_a, m.m_a, c);
		m_a = c;
	}
}
