package echozero.main;

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
		Program.log.log_message(-1, "-)cho Chrom(-");
		
		try {
			GameUIInput ginp;
			
			ai = new AWTImage(true);
			gui = new GameUI(ai);			
			new AWTInput(ai.get_frame(), new GameUIInput(gui));
		
			gui.loop();			
		}
		
		catch(Exception e) {
			System.err.println("error initializing global parameters");
			System.exit(-1);
		}
	}

}
