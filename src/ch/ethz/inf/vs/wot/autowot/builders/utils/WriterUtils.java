package ch.ethz.inf.vs.wot.autowot.builders.utils;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Utility class for writing to files
 *
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */
public class WriterUtils {

	// Keeps track of the indentation
	public Integer indent;
	
	public BufferedWriter writer;
	
	public WriterUtils() {
		indent = 0;
		writer = null;
	}
	
	/**
	 * Handles writing a String into a file
	 * @param content the String to be written
	 */
	public void writeToFile(String content) {
		try {
			for (int i = 0; i < indent; i++) writer.write("\t");
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles writing a String into a file (without indentation)
	 * @param content the String to be written
	 */
	public void writeUnformatted(String content) {
		try {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes a system-dependent newline character to the file
	 */
	public void writeNewLine() {
		try {
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops writing to a file
	 */
	public void end() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer = null;		
	}	
}