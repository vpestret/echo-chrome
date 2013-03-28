package echozero.main;

import echozero.graphics.EchoGraphicsEngine;
import echozero.host.AWTImage;
import echozero.host.AWTInput;
import echozero.ui.GameUI;
import echozero.ui.GameUIInput;
import echozero.util.Program;

public class EchoZeroMainStation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AWTImage ai;
		
		GameUI gui;
		
		
		Program.initialize_logging("application.log");
		Program.log.log_message(-1, "-)cho Chrom(-" + " " + Program.get_version());
		
		gui = null;
		try {
			ai = new AWTImage(true);
			gui = new GameUI(new EchoGraphicsEngine(ai));
			new AWTInput(ai.get_frame(), new GameUIInput(gui));
		}
		
		catch(Exception e) {
			System.err.println("error initializing global parameters: " + e.getMessage());
			System.exit(-1);
		}
		
		try {
			gui.loop();
		}
		catch(Exception e) {
			System.err.println("exception during run: " + e.getMessage());
			System.exit(-1);
		}		
	}

}
