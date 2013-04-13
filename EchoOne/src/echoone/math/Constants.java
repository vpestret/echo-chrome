package echoone.math;

public class Constants {
	private static double EPS = 1.0 / 1024 / 1024;
	private static double EPS2 = 1.0 / 1024 / 1024 / 1024 / 1024;
	public static void set_geom_epsilon(double eps) { EPS = eps; EPS2 = eps * eps; }
	
	public static boolean is_equal(double x, double y) { return (Math.abs(x - y) < EPS); }
	public static boolean is_zero(double x) { return (Math.abs(x) < EPS); }
	public static boolean is_zero2(double x) { return (Math.abs(x) < EPS2); }
	public static int compare(double x, double y) {
		double d;
		
		d = x - y;
		if(Math.abs(d) < EPS) { return 0; }
		if(d < 0) { return -1; }
		return 1;
	}
	
	public static boolean is_inside_segment(double x, double a, double b, boolean incl_a, boolean incl_b) {
		int ca;
		int cb;
		
		ca = compare(x, a); cb = compare(x, b);
		return (((ca > 0) || (ca == 0 && incl_a)) && ((cb < 0) || (cb == 0 && incl_b))); 
	}
}
