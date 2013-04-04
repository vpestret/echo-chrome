package echozero.math;

public class Matrix3 {
	private double[] m_a;
	private double[] m_b;
	private double[] m_c;
	
	public Matrix3() { m_a = new double[9]; m_b = new double[9]; m_c = new double[9]; load_identity(); }

	public double[] get_a() { return m_a; }
	
	
	public Matrix3(Matrix3 m) {
		this();
		int i;
		
		for(i = 0; i < 9; ++i) { m_a[i] = m.m_a[i]; }
	}
	
	public void print() {
		System.err.println("" + m_a.toString());
		System.err.println(String.format("%6.4f %6.4f %6.4f", m_a[0], m_a[1], m_a[2]));
		System.err.println(String.format("%6.4f %6.4f %6.4f", m_a[3], m_a[4], m_a[5]));
		System.err.println(String.format("%6.4f %6.4f %6.4f", m_a[6], m_a[7], m_a[8]));
	}
	
	public void copy(Matrix3 m) {
		System.arraycopy(m.m_a, 0, m_a, 0, 9);
	}

	public void load_identity() {
		m_a[0] = 1; m_a[1] = 0; m_a[2] = 0;
		m_a[3] = 0; m_a[4] = 1; m_a[5] = 0;
		m_a[6] = 0; m_a[7] = 0; m_a[8] = 1;
	}
	
	void set_scale(double sx, double sy) {
		m_c[0] = sx; m_c[1] = 0; m_c[2] = 0;
		m_c[3] = 0; m_c[4] = sy; m_c[5] = 0;
		m_c[6] = 0; m_c[7] = 0; m_c[8] = 1;
	}
	
	void set_translate_rotate_scale(double scale_x, double scale_y, double alpha, double dx, double dy) {
		double cs;
		double sn;
		
		cs = Math.cos(alpha);
		sn = Math.sin(alpha);
		m_c[0] = cs * scale_x; m_c[1] = -sn; m_c[2] = -dx;
		m_c[3] = sn; m_c[4] = cs * scale_y; m_c[5] = -dy;
		m_c[6] = 0; m_c[7] = 0; m_c[8] = 1;
	}
	
	void set_translate_rotate(double alpha, double dx, double dy) {
		double cs;
		double sn;
		
		cs = Math.cos(alpha);
		sn = Math.sin(alpha);
		m_c[0] = cs; m_c[1] = -sn; m_c[2] = -dx;
		m_c[3] = sn; m_c[4] = cs; m_c[5] = -dy;
		m_c[6] = 0; m_c[7] = 0; m_c[8] = 1;
	}
	
	void set_translate(double dx, double dy) {
		m_c[0] = 1; m_c[1] = 0; m_c[2] = -dx;
		m_c[3] = 0; m_c[4] = 1; m_c[5] = -dy;
		m_c[6] = 0; m_c[7] = 0; m_c[8] = 1;
	}
	
	void set_rotate(double alpha) {
		double cs;
		double sn;
		
		cs = Math.cos(alpha);
		sn = Math.sin(alpha);
		m_c[0] = cs; m_c[1] = -sn; m_c[2] = 0;
		m_c[3] = sn; m_c[4] = cs; m_c[5] = 0;
		m_c[6] = 0; m_c[7] = 0; m_c[8] = 1;
	}

	
	public Matrix3 scale(double sx, double sy) {
		double[] t;
		
		set_scale(sx, sy);
		multiply(m_c, m_a, m_b);
		t = m_a;
		m_a = m_b;
		m_b = t;
		return this;
	}
	
	public Matrix3 translate_rotate(double alpha, double dx, double dy) {
		double[] t;
		
		set_translate_rotate(alpha, dx, dy);
		multiply(m_c, m_a, m_b);
		t = m_a;
		m_a = m_b;
		m_b = t;
		return this;
	}
	
	public Matrix3 translate_rotate_scale(double scale_x, double scale_y, double alpha, double dx, double dy) {
		double[] t;
		
		set_translate_rotate_scale(scale_x, scale_y, alpha, dx, dy);
		multiply(m_c, m_a, m_b);
		t = m_a;
		m_a = m_b;
		m_b = t;
		return this;
	}
	
	
	public Matrix3 translate(double dx, double dy) {
		double[] t;
		
		set_translate(dx, dy);
		multiply(m_c, m_a, m_b);
		t = m_a;
		m_a = m_b;
		m_b = t;
		return this;		
	}
	
	public Matrix3 rotate(double alpha) {
		double[] t;
		
		set_rotate(alpha);
		multiply(m_c, m_a, m_b);
		t = m_a;
		m_a = m_b;
		m_b = t;
		return this;
	}

	
	
	public Matrix3 multiply_left(Matrix3 m) {
		double[] c;
		
		c = new double[9];
		multiply(m.m_a, m_a, c);
		m_a = c;
		return this;
	}
	
	public Matrix3 multiply_right(Matrix3 m) {
		double[] c;
		
		c = new double[9];
		multiply(m_a, m.m_a, c);
		m_a = c;
		return this;
	}
	
	public void multiply_vector3(Matrix3 m, double v[], double r[]) {
		r[0] = m_a[0] * v[0] + m_a[1] * v[1] + m_a[2] * v[2];
		r[1] = m_a[3] * v[0] + m_a[4] * v[1] + m_a[5] * v[2];
		r[2] = m_a[6] * v[0] + m_a[7] * v[1] + m_a[8] * v[2];
	}
	
	public Vector2 multiply_vector2h(Vector2 vec) {
		double z;
		double r1;
		double r2;
		double[] v;
		
		v = vec.get_x();
		r1 = m_a[0] * v[0] + m_a[1] * v[1] + m_a[2];
		r2 = m_a[3] * v[0] + m_a[4] * v[1] + m_a[5];
		z = m_a[6] * v[0] + m_a[7] * v[1] + m_a[8];
		return new Vector2(r1 / z, r2 / z);
	}

	void multiply(double[] a, double[] b, double[] c) {
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
	
}
