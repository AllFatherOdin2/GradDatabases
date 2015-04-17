/**
 * 
 */
package undoRedoLogging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DModica
 * @author JBosworth
 *
 */
public class Tuple {
	List<String> data = new ArrayList<String>();
	
	/**
	 * Basic Constructor
	 * @param data Data to insert into the tuple
	 */
	public Tuple(List<String> data){
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public List<String> getData() {
		Date date = new Date();
		
		String logValue = "READ, " + data.get(0) + ", " + date.getTime();
		Log.addLog(logValue);
		
		return data;
	}
	
	public void updateData(int index, String newValue){
		Date date = new Date();
		
		String logValue = "READ, " + data.get(0) + ", " + date.getTime();
		Log.addLog(logValue);
		
		String oldValue = data.get(index);
		data.set(index, newValue);
		
		logValue = "WRITE, " + data.get(0) + ", " + index + ", " + oldValue  + ", " + data.get(index) + ", " + date.getTime();
		Log.addLog(logValue);
	}
}
