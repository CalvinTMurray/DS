/**
 * The logger logs operations that are performed during MST construction
 * and broadcasts
 */
package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class Logger {
	
	private static Logger log;
	private static BufferedWriter writer;
	
	public Logger(){
		try {
			writer = new BufferedWriter(new FileWriter(new File("log"), true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static synchronized void bs(String str) {

		try {
			writer.write("bs " + str + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void elected() {

	}

	public static synchronized void added() {

	}
	
	public static synchronized void close() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
