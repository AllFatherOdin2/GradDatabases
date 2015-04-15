package undoRedoLogging;

import java.util.ArrayList;
import java.util.List;

public class Log {
	private static List<String> log = new ArrayList<String>();
	
	public static void addLog(String entry){
		log.add(entry);
	}
	
}
