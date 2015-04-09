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
import java.util.List;

import ProjectCode.indexMechException;

public class indexBucketImpl {
	private static String direct = System.getProperty("user.dir") + "\\";
	private static String next = "";
	private static final String INPUT_FILE = direct + "index.txt";
	private static final String OVERFLOW_TITLE = "OVERFLOW";
	private static List<Bucket> buckets;
	
	public static void main(String[] args) throws indexMechException{
		
		buckets = getBuckets();

		
		//----------------10
		put("KeyTest", "DataValueTest");
		put("Key2Test", "DataValueTest");
		
		put("testKey", "DataValueTest1");
		put("testKey2", "DataValueTest1");
		
		put("KeyWTest", "DataValueTest2");
		put("KeyW2Test", "DataValueTest2");
		
		put("KeyXTest", "DataValueTest3");
		put("KeyX2Test", "DataValueTest3");
		
		put("KeyYTest", "DataValueTest4");
		put("KeyY2Test", "DataValueTest4");
		//-----------------10

		put("KeyTest", "testDataValue");
		
		put("overflowText", "DataValueTest");
		put("overflow2text", "DataValueTest");
		put("overflow3text", "DataValueTest");
		

		printBuckets();

		System.out.println(next);
		System.out.println(Integer.MAX_VALUE);
	}
	
	public indexBucketImpl(){}
	
	/**
	 * Reads in the index file as a byte array and returns all of the buckets that were found.
	 * @return List of buckets stored in index
	 */
	public static List<Bucket> getBuckets(){
		
		byte[] bytes = null;
		List<Bucket> buckets = null;
		
		
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			if(bytes.length <= 0){
				buckets = new ArrayList<Bucket>();
			}
			else{
				buckets = ByteStringManipulator.byteToBucket(bytes);
				
			}
		} catch (IOException e) {
			//File does not exist, therefore create
			buckets = new ArrayList<Bucket>();
		}
		
		return buckets;
	}
	
	/**
	 * Will read in data from a byte array, and build a text file containing the data, with the key as the name of the file
	 * If the file exists and is not full, the key-datavalue pair will be added to the file, and the key added to the index
	 * If the file exists and is "full", a file will be split in round-robin fashion. If the overflowing file is not the
	 * one to split, then an overflow file will be generated and the data stored there until the file is split by more
	 * overflows. At that point, the data will be put into the new split file, and removed from the overflow file.
	 * 
	 * @param key rid used for counting records
	 * @param dataValue Data is used in a hash function, which returns value of "bucket" to put key-datavalue pair in.
	 * @throws indexMechException 
	 */
	public static void put(String key, String dataValue) throws indexMechException{
		//final String INPUT_FILE = direct + "index" + hash(dataValue) + ".txt";
		String hashedValue = hash(dataValue);
		boolean updateNext = false;
		boolean keyAdded = false;
		boolean toOverflow = false;
		Bucket newBucket = null;
		Bucket overflowBucket = null;
		
		//The datafile to update. Will be based on Hash, unless need to overflow
		String targetFile = hashedValue;
		

		//get last element, to check if it is overflow
		if(buckets.size() > 0){
			Bucket possibleOverflow = buckets.get(buckets.size()-1); 
			if(possibleOverflow.getIndex().equals(OVERFLOW_TITLE)){
				overflowBucket = possibleOverflow;
				buckets.remove(buckets.size()-1); //Remove overflow bucket from major list to add to end later
			}
			
			//if index file exists, find where to put this new item
			for(Bucket bucket : buckets){
				//update next if needed
				if(updateNext && bucket.getIndex().equals(next) == false){
					next = hashedValue;
					updateNext = false;
				}
				
				if(keyAdded){
					break;
				}
				
				if(bucket.isCorrectBucket(hashedValue)){
					try {
						bucket.addKey(key);
						keyAdded = true;
					} catch (BucketOverflowException e) {
						//This bucket is the next one to be split, so allow it to.
						if(next.equals(bucket.getIndex()) && !updateNext){
							updateNext = true;
							//Create new List of keys
							List<String> tempList = new ArrayList<String>();
							tempList.add(key);
							//Create new Bucket with the same hash Value + split indicator, but this new key
							newBucket = new Bucket(hashedValue + "s", tempList);
							targetFile += "s"; //There is a split file, find it.
						}
						//This bucket is not next, therefore add this to overflow.
						else{
							targetFile = OVERFLOW_TITLE;
							if(overflowBucket == null){
								//the overflow bucket does not exist
								
								//Create new List of keys
								List<String> tempList = new ArrayList<String>();
								tempList.add(key);
								overflowBucket = new Bucket(OVERFLOW_TITLE, tempList);
								keyAdded = true;
							}
							else{
								//the overflow bucket already exists
								try {
									overflowBucket.addKey(key);
									keyAdded = true;
								} catch (BucketOverflowException | BucketFilledException e1) {
									e1.printStackTrace();
								}
							}
						}
					} catch(BucketFilledException e){
						//This bucket is filled, but another bucket of the same hash should exist elsewhere
						//so ignore this bucket entirely
						toOverflow = true;
						targetFile += "s"; //There is a split file, find it.
						hashedValue += "s";
						continue;
					}
				}
			}
		}
		else{
			next = hashedValue;
		}
		
		//if the data hasnt been added to a bucket yet, add it. 
		//And the key hasnt been added to a bucket
		if(newBucket == null && !keyAdded && !toOverflow){
			List<String> tempList = new ArrayList<String>();
			tempList.add(key);
			
			newBucket = new Bucket(hashedValue, tempList);
		}
		
		if(newBucket != null){
			//add the new bucket to the list
			buckets.add(newBucket);
		}
		
		//If overflow bucket exists, tack it on to bucket array again.
		if(overflowBucket != null){
			
			if(toOverflow){
				//this bucket needs to be added to overflow, 
				//because it is not the next target, and overflow exists already
				try {
					overflowBucket.addKey(key);
					targetFile = OVERFLOW_TITLE;
				} catch (BucketOverflowException | BucketFilledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			buckets.add(overflowBucket);
		}
		
		addToTargetFile(targetFile, key, dataValue);
			
	}

	private static void addToTargetFile(String targetFile, String key, String dataValue) {
		String INPUT_FILE = direct + targetFile + ".txt";

		byte[] bytes = null;
		String[][] indexFile = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			indexFile = byteToKeyDatavalue(bytes);

		} catch (IOException e) {
			//File does not exist, therefore create in next step
			indexFile = new String[0][0];
		}
		
		File entry;
		FileOutputStream fop = null;

		entry = new File(INPUT_FILE);
		try {
			fop = new FileOutputStream(entry);
			String strToByte = "";
			for(String[] index : indexFile){
				strToByte += index[0] + "`" + index[1] + "\n";
			}
			strToByte += dataValue + "`" + key;
			
			byte[] data = strToByte.getBytes();
			
			fop.write(data);
			fop.flush();
			fop.close();
			
		} catch (IOException e) {
			e.printStackTrace();
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
		String str = ByteStringManipulator.byteToStringArray(data);
		String[] indexPairs = str.split("\n"); //prior format key`dataValue~ (repeat)
		
		String[][] indexValuePairs = new String[indexPairs.length][2];
		for(int x = 0; x < indexPairs.length; x++){ //format key`dataValue
			indexValuePairs[x] = indexPairs[x].split("`");
		}
		return indexValuePairs;
	}
	
	public static void printBuckets(){
		File entry;
		FileOutputStream fop = null;

		entry = new File(direct + "index.txt");
		try {
			fop = new FileOutputStream(entry);

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
			
			List<Bucket> buckets = ByteStringManipulator.byteToBucket(bytes);
			
			for(Bucket bucket : buckets){
				
				if(bucket.getIndex().compareTo(hash(dataValue)) == 0){
					result = bucket.getKeys();
				}
			}
			
			if(result.size() > 0){
				return result;
			}
			
			throw new indexMechException("That Data Value does not exist");
			
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
		List<String> result = new ArrayList<String>();
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			List<Bucket> buckets = ByteStringManipulator.byteToBucket(bytes);
			
			for(Bucket bucket : buckets){
				
				if(bucket.getIndex().compareTo(hash(dataValue)) == 0){
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
			/*
			for(String[] record : notDataValue){
				put(record[0],record[1]);
			}
			*/
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
