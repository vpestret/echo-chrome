package echozero.graphics;

import echozero.math.Matrix3;
import echozero.util.Program;

public class EchoGraphicsEngine {
	public static final int MATRIX_STACK_DEPTH = 256;
	private HostGraphics m_hg;
	private int m_curr_view;
	private Matrix3 m_curr_matrix;
	private Matrix3[] m_view_matrix;
	
	
	public EchoGraphicsEngine(HostGraphics hg) {
		m_hg = hg;
		m_curr_view = 0;
		m_view_matrix = new Matrix3[MATRIX_STACK_DEPTH];
		m_view_matrix[0] = new Matrix3();
		m_curr_matrix = m_view_matrix[0];
	}
	
	public void load_host_projection() {
		
	}
	
	public void push_matrix() {
		if(m_curr_view == MATRIX_STACK_DEPTH - 1) { Program.log.error("EchoGraphicsEngine: matrix stack overflow"); }
		
		m_view_matrix[m_curr_view + 1] = m_view_matrix[m_curr_view];
		++m_curr_view;
	}
	
	public void pop_matrix() {
		if(m_curr_view == 0) { Program.log.error("EchoGraphicsEngine: matrix stack empty"); }
		
		--m_curr_view;
		m_curr_matrix = m_view_matrix[m_curr_view];
	}
	
	public void draw_prim_list(PrimitiveList pl) { pl.draw(m_curr_matrix, m_hg); }
	
	/* lower-level transparent functions */
	public void clear_all() { m_hg.clear_all(); }
	public void set_current_buffer() { m_hg.set_current_buffer(); }
	public void switch_buffers() { m_hg.switch_buffers(); }
}
