/**
 * 
 */
package undoRedoLogging;

import java.util.ArrayList;
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
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<String> data) {
		this.data = data;
	}
	
	public void updateData(int index, String newValue){
		String oldValue = data.get(index);
		data.set(index, newValue);
		
		String logValue = "WRITE, " + ", " + ", " + ", ";
		Log.addLog(logValue);
	}
}
