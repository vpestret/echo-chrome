package echozero.util;

public class Program {
	private static final double version = 0.001;
	
	public static Logging log;
	
	public static void initialize_logging(String path) { log = new Logging(path); }
	public static double get_version() { return version; }
}
