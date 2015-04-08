package indexMechTake2;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ProjectCode.indexMechException;

public class IndexMech2 {
	private static String direct = System.getProperty("user.dir") + "\\";
	private static String next = "";
	
	public static void main(String[] args) throws indexMechException{
		/*
		put("KeyTest", "DataValueTest");
		put("Key2Test", "DataValueTest");
		
		put("testKey", "testDataValue");
		put("testKey2", "testDataValue");
		
		put("KeyXTest", "ValueDataTest");
		put("KeyX2Test", "ValueDataTest");
		*/
		
		List<String> keys = get("ValueDataTest");
		System.out.println(keys.toString());

		keys = get("testDataValue");
		System.out.println(keys.toString());

		keys = get("DataValueTest");
		System.out.println(keys.toString());
	}
	
	public IndexMech2(){}
	
	public IndexMech2(String custDir){
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
	public static void put(String key, String dataValue) throws indexMechException{
		//final String INPUT_FILE = direct + "index" + hash(dataValue) + ".txt";
		final String INPUT_FILE = direct + "index.txt";
		final int MAX_BUCKET_SIZE = Integer.MAX_VALUE;
		final String OVERFLOW_TITLE = "overflow";
		
		
		byte[] bytes = null;
		HashMap<String, List<String>> indexFile = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			indexFile = byteToKeyDatavalue(bytes);

		} catch (IOException e) {
			//File does not exist, therefore create in next step
			//which happens anyway
			indexFile = new HashMap<String,List<String>>();
		}
		
		File entry;
		FileOutputStream fop = null;

		entry = new File(direct + "index.txt");
		try {
			fop = new FileOutputStream(entry);
			String strToByte = "";
			if(indexFile.size() > 0){
				boolean keyAdded = false;
				boolean overflowFlag = false;
				boolean overflowExists = false;
				boolean updateNext = false;

				Iterator<Entry<String, List<String>>> entries = indexFile.entrySet().iterator();
				while(entries.hasNext()){
					Map.Entry<String, List<String>> bucket = entries.next();
					
					if(next.length() <= 0 || updateNext){
						next = bucket.getKey();
						updateNext = false;
					}
					
					//Add index to front of tuple;
					if(bucket.getKey().equals(OVERFLOW_TITLE) && !keyAdded){
						strToByte += hash(dataValue) + "`" + key + "\n";
						keyAdded = true;
					}
					strToByte += bucket.getKey() + "`";
					
					//Check if this is the index we want
					if(bucket.getKey().equals(hash(dataValue))){
						if(bucket.getValue().size() < MAX_BUCKET_SIZE)
						{
							//Add each Key to end of tuple
							for(String bucketKey : bucket.getValue()){
								strToByte += bucketKey + "~";
							}
							strToByte += key + "~"; //tack key onto end of current bucket
							keyAdded = true;
						}
						else{
							if(next.equals(bucket.getKey())){
								strToByte += "\n" + bucket.getKey() + "`" + key + "~";
								keyAdded = true;
								updateNext = true;
							}
							else{
								overflowFlag = true;
							}
						}
						
					}
					else{
						//Add each Key to end of tuple
						for(String bucketKey : bucket.getValue()){
							strToByte += bucketKey + "~";
						}
						if(overflowFlag){
							if(bucket.getKey() == OVERFLOW_TITLE){
								strToByte += key;
								keyAdded = true;
								overflowExists = true;
							}
						}
					}
					
					strToByte = strToByte.substring(0, strToByte.length()-1) + "\n"; //remove tilde add new line
				}
				
				if(overflowFlag && !overflowExists){
					strToByte += OVERFLOW_TITLE + "`" + key;
				}
				else if(!keyAdded){
					strToByte += hash(dataValue) + "`" + key + "\n";
				}
				else{
					strToByte = strToByte.substring(0, strToByte.length()-1); //remove ending new line
				}
			}
			else{
				//If index file does not exist, create it with this entry.
				strToByte += hash(dataValue) + "`" + key;
				
			}

			
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
	public static List<String> get(String dataValue) throws indexMechException{
		final String INPUT_FILE = direct + "index.txt";
		
		byte[] bytes = null;
		List<String> result = new ArrayList<String>();
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			HashMap<String, List<String>> indexFile = byteToKeyDatavalue(bytes);
			
			Iterator<Entry<String, List<String>>> entries = indexFile.entrySet().iterator();
			while(entries.hasNext()){
				Map.Entry<String, List<String>> bucket = entries.next();
				
				if(bucket.getKey().compareTo(hash(dataValue)) == 0){
					result = bucket.getValue();
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
			
			HashMap<String, List<String>> indexFile = byteToKeyDatavalue(bytes);
			
			Iterator<Entry<String, List<String>>> entries = indexFile.entrySet().iterator();
			while(entries.hasNext()){
				Map.Entry<String, List<String>> bucket = entries.next();
				
				if(bucket.getKey().compareTo(hash(dataValue)) == 0){
					//notDataValue = bucket.getValue();
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
	 * @throws indexMechException 
	 */
	private static HashMap<String, List<String>> byteToKeyDatavalue(byte[] data) throws indexMechException{
		String str = byteToStringArray(data);
		String[] bucketArray = str.split("\n"); //return each bucket
		HashMap<String, List<String>> indexKeyPairs = new HashMap<String, List<String>>();
		
		for(String bucket: bucketArray){
			String[] indexArray = bucket.split("`"); //returns the hashed index value. should only be one
			if(indexArray.length > 2){
				throw new indexMechException("Too many indexes");
			}
			String index = indexArray[0];
			
			//prior format index`key1~key2~...~key10
			String[] keyArray = indexArray[1].split("~"); //
			
			List<String> keys = Arrays.asList(keyArray);
			
			indexKeyPairs.put(index, keys);
		}
		
		return indexKeyPairs;
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
