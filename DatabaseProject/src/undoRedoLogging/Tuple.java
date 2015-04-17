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
		return data;
	}
	
	/**
	 * updates the value stored at the provided index within the tuple with newValue
	 * @param attributeIndex
	 * @param newValue
	 */
	public void updateData(int attributeIndex, String newValue, int tupleIndex){
		Date date = new Date();
		String oldValue = data.get(attributeIndex);
		data.set(attributeIndex, newValue);
		
		String logValue = "WRITE, " + tupleIndex + ", " + attributeIndex + ", " + oldValue  + ", " + data.get(attributeIndex) + ", " + date.getTime();
		Log.addLog(logValue);
	}
}
