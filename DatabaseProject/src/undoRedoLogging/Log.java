package undoRedoLogging;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DModica
 * @author JBosworth
 *
 */
public class Log {
	private static List<String> log = new ArrayList<String>();
	
	/**
	 * Adds a log statement to the log list
	 * @param entry Log statement to add
	 */
	public static void addLog(String entry){
		log.add(entry);
	}
	
	/**
	 * Returns all logs
	 * @return List of current logs
	 */
	public static List<String> getLog(){
		return log;
	}
	
	/**
	 * Clears the log
	 */
	public static void clearLog(){
		log = new ArrayList<String>();
	}
	
	/**
	 * Writes the entire log to a file
	 */
	public static void writeToFile(){
		
	}
}
