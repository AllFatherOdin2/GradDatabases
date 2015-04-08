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

public class indexBucketImpl {
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
	
	public indexBucketImpl(){}
	
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
		final String OVERFLOW_TITLE = "overflow";
		final String hashedValue = hash(dataValue);
		boolean updateNext = false;
		
		byte[] bytes = null;
		List<Bucket> buckets = null;
		
		
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			buckets = ByteStringManipulator.byteToBucket(bytes);

		} catch (IOException e) {
			//File does not exist, therefore create
			buckets = new ArrayList<Bucket>();
		}
		
		File entry;
		FileOutputStream fop = null;

		entry = new File(direct + "index.txt");
		try {
			fop = new FileOutputStream(entry);
			if(buckets.size() > 0){
				//if index file exists, find where to put this new item
				for(Bucket bucket : buckets){
					//update next if needed
					if(next.length() <= 0 || updateNext){
						next = hashedValue;
						updateNext = false;
					}
					
					if(bucket.isCorrectBucket(hashedValue)){
						try {
							bucket.addKey(key);
						} catch (BucketOverflowException e) {
							//This bucket is the next one to be overflowed, so allow it to.
							if(next.equals(bucket.getIndex())){
								updateNext = true;
								//Create new List of keys
								List<String> tempList = new ArrayList<String>();
								tempList.add(key);
								//Create new Bucket with the same hash Value, but this new key
								Bucket newBucket = new Bucket(hashedValue, tempList);
								//Add bucket to overall List
								buckets.add(newBucket);
							}
							//This bucket is not next, therefore add this to overflow.
							else{
								//TODO make overflow a thing.
							}
						} catch(BucketFilledException e){
							//This bucket is filled, but another bucket of the same hash should exist elsewhere
							//so ignore this bucket entirely
							continue;
						}
					}
				}
			}
			else{
				//If index file does not exist, create it with this entry.
				List<String> tempList = new ArrayList<String>();
				tempList.add(key);
				Bucket tempBucket = new Bucket(hashedValue, tempList);
				buckets.add(tempBucket);
			}

			
			
			//Write buckets to index File
			String strToByte = "";
			for(Bucket bucket:buckets){
				strToByte += bucket.toString() + "\n";
			}
			strToByte = strToByte.substring(0, strToByte.length()-1); //remove extra new line
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
