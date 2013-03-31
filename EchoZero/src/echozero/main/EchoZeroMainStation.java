package echozero.main;

import echozero.game.GameState;
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
		GameState game;
		GameUI gui;
		
		
		Program.initialize_logging("application.log");
		Program.log.log_message(-1, "-)cho Chrom(-" + " " + Program.get_version());
		
		gui = null;
		game = null;
		
		try {
			game = new GameState();
			ai = new AWTImage(true, 1280, 1024);
			gui = new GameUI(new EchoGraphicsEngine(ai), game);
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
