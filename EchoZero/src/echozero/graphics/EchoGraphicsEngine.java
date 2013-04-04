package echozero.graphics;

import echozero.graphics.host.HostGraphics;
import echozero.graphics.host.HostGraphicsCapabilities;
import echozero.graphics.primitives.PrimitiveList;
import echozero.math.Matrix3;
import echozero.math.Vector2;
import echozero.util.Program;

public class EchoGraphicsEngine {
	public static final int MATRIX_STACK_DEPTH = 256;
	private HostGraphics m_hg;
	private HostGraphicsCapabilities m_cap;
	private Matrix3 m_host_matrix;
	private int m_curr_view;
	private Matrix3 m_curr_matrix;
	private Matrix3[] m_view_matrix;
	
	
	public EchoGraphicsEngine(HostGraphics hg) {
		int i;
		
		m_hg = hg;
		m_curr_view = 0;
		m_view_matrix = new Matrix3[MATRIX_STACK_DEPTH];
		for(i = 0; i < MATRIX_STACK_DEPTH; ++i) { m_view_matrix[i] = new Matrix3(); }
		m_curr_matrix = new Matrix3();
		notify_display_change();
	}
	
	public void load_host_projection() {
		double w;
		double h;
		
		w = m_cap.get_width();
		h = m_cap.get_height();
		if(w < h) { m_host_matrix = new Matrix3().translate_rotate_scale(h, -h, 0, 0, -h); }
		else { m_host_matrix = new Matrix3().translate_rotate_scale(w, -w, 0, 0, -w); }
	}
	
	public void notify_display_change() {
		m_cap = m_hg.get_caps();
		load_host_projection();
		change_view();	
	}
	
	public void change_view() {
		m_curr_matrix.copy(m_host_matrix);
		m_curr_matrix.multiply_right(m_view_matrix[m_curr_view]);
	}
	
	public void translate(double x, double y) { 
		m_view_matrix[m_curr_view].translate(-x, -y); 
		change_view(); 
	}
	
	public void translate(Vector2 v) { 
		m_view_matrix[m_curr_view].translate(-v.get_x()[0], -v.get_x()[1]);
		change_view();
	}
	
	public void rotate(double alpha) { 
		m_view_matrix[m_curr_view].rotate(alpha);
		change_view();
	}
	
	public void scale(double sx, double sy) { 
		m_view_matrix[m_curr_view].scale(sx, sy);
		change_view();
	}
	
	public void push_matrix() {
		if(m_curr_view == MATRIX_STACK_DEPTH - 1) { Program.log.error("EchoGraphicsEngine: matrix stack overflow"); }
		
		m_view_matrix[m_curr_view + 1].copy(m_view_matrix[m_curr_view]);
		++m_curr_view;
		change_view();
	}
	
	public void pop_matrix() {
		if(m_curr_view == 0) { Program.log.error("EchoGraphicsEngine: matrix stack empty"); }
		--m_curr_view;
		change_view();
	}
	
	public void draw_prim_list(PrimitiveList pl) {
		pl.draw(m_curr_matrix, m_hg); 
	}
	
	/* lower-level transparent functions */
	public void clear_all() { m_hg.clear_all(); }
	public void set_current_buffer() { m_hg.set_current_buffer(); }
	public void switch_buffers() { m_hg.switch_buffers(); }
	
	public void draw_fps(double fps) {
		m_hg.set_color(1, 1, 1, 0);
		m_hg.text(m_cap.get_width() - 100, 50, String.format("%5.2f FPS", fps));
	}
}
