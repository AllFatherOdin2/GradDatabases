package ProjectCode;
/**
 * A valuestore implementation using multiple files for storage. 
 * File size is completely dynamic, and is based on the size of the input data
 */

/**
 * @author JBosworth
 * @author DModica
 *
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class indexMechImpl {
	private static String direct = System.getProperty("user.dir") + "\\";
	
	public indexMechImpl(){}
	
	public indexMechImpl(String custDir){
		direct = custDir + "\\";
	}
	
	/**
	 * Getter Function
	 * 
	 * @return the current working directory
	 */
	public String getWorkingDirectory(){
		return direct;
	}
	
	/**
	 * Will read in data from a byte array, and build a text file containing the data, with the key as the name of the file
	 * If the file already exists, the data will be erased and overwritten
	 * 
	 * @param key rid used for counting records
	 * @param dataValue Data is used in a hash function, which returns value of "bucket" to put key-datavalue pair in.
	 * @throws indexMechException 
	 */
	public void put(String key, String dataValue) throws indexMechException{
		//final String INPUT_FILE = direct + "index" + hash(dataValue) + ".txt";
		final String INPUT_FILE = direct + "index.txt";
		
		//checks to see if the data value already exists, and throws an error if it does.
		boolean doopFlag = false;
		
		try{
			List<String> gottenResult = this.get(dataValue);
			
			doopFlag = gottenResult.contains(key);
		}catch(Exception e){}
		
		if(doopFlag){
			throw new indexMechException("The Datavalue already indexes this key");
		}
		
		byte[] bytes = null;
		String[][] indexFile = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			indexFile = byteToKeyDatavalue(bytes);

		} catch (IOException e) {
			//File does not exist, therefore create in next step
			//which happens anyway
			indexFile = new String[0][0];
		}
		
		File entry;
		FileOutputStream fop = null;

		entry = new File(direct + "index" + hash(dataValue) + ".txt");
		try {
			fop = new FileOutputStream(entry);
			String strToByte = "";
			for(String[] index : indexFile){
				strToByte += index[0] + "`" + index[1] + "~";
			}
			strToByte += key + "`" + dataValue + "~";
			
			byte[] data = strToByte.getBytes();
			
			fop.write(data);
			fop.flush();
			fop.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Will look for a file with the same name as the key, 
	 * and read the data from it, returning it as a byte[]
	 * 
	 * @param dataValue Datavalue to search for in a file based on hashed value
	 * @return Key stored in the text file associated with given datavalue
	 * @throws indexMechException 
	 */
	public List<String> get(String dataValue) throws indexMechException{
		final String INPUT_FILE = direct + "index" + hash(dataValue) + ".txt";
		
		byte[] bytes = null;
		List<String> result = new ArrayList<String>();
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			String[][] indexFile = byteToKeyDatavalue(bytes);
			
			for(String[] index : indexFile){
				if(index[1].compareTo(dataValue) == 0){
					result.add(index[0]);
				}
			}
			
			if(result.size() > 0){
				return result;
			}
			
			throw new indexMechException("That Data Value does not exist");
			
			//System.out.println(INPUT_FILE);
			//System.out.println("Available bytes from the file: "+inputStream.available());
			
			
			//System.out.println("Read Bytes: "+bytesread);
			//System.out.println(Arrays.toString(bytes));
			
		} catch(IOException e){
			throw new indexMechException("No index file could be found");
		}
		
	}
	
	/**
	 * Deletes text file with the same name as the given key
	 * 
	 * @param dataValue Datavalue that is going to be deleted from a given bucket
	 * @throws indexMechException 
	 */
	public void remove(String dataValue) throws indexMechException{

		final String INPUT_FILE = direct + "index" + hash(dataValue) + ".txt";
		
		byte[] bytes = null;
		List<String[]> notDataValue = new ArrayList<String[]>();
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			String[][] indexFile = byteToKeyDatavalue(bytes);
			
			for(String[] index : indexFile){
				if(index[1].compareTo(dataValue) != 0){
					notDataValue.add(index);
				}
			}
			
			//throw new indexMechException("That Data Value does not exist");
			
			
		} catch(IOException e){
			throw new indexMechException("No index file could be found");
		}

		
		File entry = new File(INPUT_FILE);
		
		if(entry.exists()){
			entry.delete();
			
			for(String[] record : notDataValue){
				put(record[0],record[1]);
			}
		}
	}
	
	/**
	 * This function is used to convert a byte array into a 2D String array
	 * This is necessary because the indexed records are stored as a single string of text, and need to be split
	 * apart before they can be used
	 * 
	 * @param data The byte array to split into strings
	 * @return
	 */
	private static String[][] byteToKeyDatavalue(byte[] data){
		String str = byteToStringArray(data);
		String[] indexPairs = str.split("~"); //prior format key`dataValue~ (repeat)
		
		String[][] indexValuePairs = new String[indexPairs.length][2];
		for(int x = 0; x < indexPairs.length; x++){ //format key`dataValue
			indexValuePairs[x] = indexPairs[x].split("`");
		}
		return indexValuePairs;
	}
	
	/**
	 * Helper function for byteToKeyDatavalue()
	 * Splits a byte array into a String using String.split()
	 * Byte array is formated using Arrays.toString() to create a CSV string that can be split on the sequence ", "
	 * 
	 * @param data Byte array to split into a string
	 * @return
	 */
	private static String byteToStringArray(byte[] data){
		String result = Arrays.toString(data);
		//Will be in array form, so this removes "[" and "]"
		result = result.substring(1, result.length()-1); 
		
		String[] resultArray = result.split(", ");
		
		//Testing
		result = "";
		for(String s : resultArray){
			result += (char)Integer.parseInt(s);
		}
		
		return result;
	}
	
	/**
	 * Simple hash function to sort datavalues into buckets
	 * 
	 * @param dataValue Value to be hashed
	 * @return
	 */
	private static String hash(String dataValue) {
		if(dataValue.length() > 4){
			dataValue = dataValue.substring(0, 4);
		}
		
		int hash = 19;
		for (int i = 0; i < dataValue.length(); i++){
			hash = hash * 53 + (int)dataValue.charAt(i);
		}
		return ""+hash;
	}
}
