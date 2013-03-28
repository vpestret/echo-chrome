package echozero.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logging {
	private int m_log_level;
	private long m_start;
	private FileWriter m_fw;
	private BufferedWriter m_bw;
	
	public Logging(String filename) {
		try { m_fw = new FileWriter(filename, false); }
		catch(IOException e) {
			System.err.println("cannot open file `" + filename + "` for logging");
			System.exit(-1);
		}
		
		m_bw = new BufferedWriter(m_fw);
		m_start = System.nanoTime();
		m_log_level = 1;
	}
	
	public Logging() { this("application.log"); }
	
	public void set_log_level(int lvl) { if(lvl >= 0) { m_log_level = lvl; } }
	
	public void error(String msg) {
		log_message(-1, "*** ERROR ***: " + msg);
		System.exit(-1);
	}
	
	public void log_message(int lvl, String msg) {
		long dt;
		
		if(lvl >= 0 && m_log_level < lvl) { return; }
		
		dt = System.nanoTime() - m_start;
		System.err.println(dt + " : " + msg);
		try { 
			m_bw.write(dt + " : " + msg); 
			m_bw.newLine();
			m_bw.flush();
		}
		catch(IOException e) { 
			System.err.println("message " + msg);
			System.err.println("cannot be logged");
		}
	}
}
