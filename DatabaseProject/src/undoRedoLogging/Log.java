package undoRedoLogging;

import java.util.ArrayList;
import java.util.List;

public class Log {
	private static List<String> log = new ArrayList<String>();
	
	public static void addLog(String entry){
		log.add(entry);
	}
	
	public static List<String> getLog(){
		return log;
	}
	
	public static void clearLog(){
		log = new ArrayList<String>();
	}
}
