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
	
	public static void main(String[] args) throws indexMechException{
		byte[] bytes = new byte[2];
		bytes[0] = (byte)'b';
		bytes[1] = (byte)'q';
		System.out.println(get("Hello"));
	}
	
	public indexMechImpl(){}
	
	public indexMechImpl(String custDir){
		direct = custDir + "\\";
	}
	
	/**
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
	 * @param key Key to write data under
	 * @param data Data to record
	 * @throws indexMechException 
	 */
	@SuppressWarnings("resource")
	public static void put(String key, String dataValue) throws indexMechException{
		final String INPUT_FILE = direct + "indices.txt";
		
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
					if(index[1].compareTo(dataValue) == 0){
						
						//restore file to previous state
						fop.write(bytes);
						fop.flush();
						fop.close();
						throw new indexMechException("Data Value already exists in the indices");
					}
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
	
	private static String hash(String dataValue) {
		
		return null;
	}

	/**
	 * Will look for a file with the same name as the key, 
	 * and read the data from it, returning it as a byte[]
	 * 
	 * @param key File name to search for
	 * @return Data stored in the text file
	 * @throws indexMechException 
	 */
	public static List<String> get(String dataValue) throws indexMechException{
		final String INPUT_FILE = direct + "indices.txt";
		
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
	 * @param key Name of file to delete
	 * @throws indexMechException 
	 */
	public void remove(int key) throws indexMechException{

		File entry = new File(direct + key + ".txt");
		
		if(entry.exists()){
			entry.delete();
			//System.out.println("Entry Deleted");
		}
		else{
			throw new indexMechException();
		}
		
	}
	
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
	
	private static String[][] byteToKeyDatavalue(byte[] data){
		String str = byteToStringArray(data);
		String[] indexPairs = str.split("~"); //prior format key`dataValue~ (repeat)
		
		String[][] indexValuePairs = new String[indexPairs.length][2];
		for(int x = 0; x < indexPairs.length; x++){ //format key`dataValue
			indexValuePairs[x] = indexPairs[x].split("`");
			//System.out.println(indexValuePairs[x][0] + ": " + indexValuePairs[x][1]);
		}
		return indexValuePairs;
	}
}
