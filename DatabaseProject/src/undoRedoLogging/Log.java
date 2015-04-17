package undoRedoLogging;

import java.util.ArrayList;
import java.util.List;

import util.ByteStringManipulator;

/**
 * @author DModica
 * @author JBosworth
 *
 */
public class Log {
	private static List<String> log = new ArrayList<String>();
	private static final String direct = System.getProperty("user.dir") + "\\";
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
		String logFile = direct + "log";
		ByteStringManipulator.addStringListToTargetFile(logFile, log);
		clearLog();
	}
	
	/**
	 * Read in log file as a list of strings
	 * @return List of strings that contains the log data
	 * @throws LoggingException 
	 */
	public static List<String> getLogFromFile() throws LoggingException{
		String targetFile = direct + "log.txt";
		try {
			return ByteStringManipulator.byteToStringList(targetFile);
		} catch (Exception e) {
			throw new LoggingException("Log file does not exist");
		}
	}
}




