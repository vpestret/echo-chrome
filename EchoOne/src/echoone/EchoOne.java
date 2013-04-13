package echoone;

public class EchoOne {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		echoone.math.Constants.set_geom_epsilon(1.0 / 1024 / 1024);
		echoone.geom.Limits.set_range(1 << 28);
	}

}
