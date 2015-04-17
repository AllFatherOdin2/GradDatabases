package util;

import indexMechTake2.Bucket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ProjectCode.indexMechException;

public class ByteStringManipulator {

	/**
	 * This function is used to convert a byte array into a 2D String array
	 * This is necessary because the indexed records are stored as a single string of text, and need to be split
	 * apart before they can be used
	 * 
	 * @param data The byte array to split into strings
	 * @return
	 * @throws indexMechException 
	 */
	public static List<Bucket> byteToBucket(byte[] data){
		String str = byteToStringArray(data);
		String[] bucketArray = str.split("\n"); //return each bucket
		List<Bucket> buckets = new ArrayList<Bucket>();
		
		for(String bucket: bucketArray){
			String[] indexArray = bucket.split("`"); //returns the hashed index value. should only be one
			if(indexArray.length > 2){
				//throw new indexMechException("Too many indexes");
			}
			String index = indexArray[0];
			
			//prior format index`key1~key2~...~key10
			String[] keyArray = indexArray[1].split("~"); //
			
			List<String> keys = new LinkedList<String>(Arrays.asList(keyArray));
			
			buckets.add(new Bucket(index, keys));
		}
		
		return buckets;
	}
	
	/**
	 * Helper function for byteToKeyDatavalue()
	 * Splits a byte array into a String using String.split()
	 * Byte array is formated using Arrays.toString() to create a CSV string that can be split on the sequence ", "
	 * 
	 * @param data Byte array to split into a string
	 * @return
	 */
	public static String byteToStringArray(byte[] data){
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
	 * Converts a byte array into a list of lists of strings in CSV format.
	 * 
	 * @param data Input byte array
	 * @return List of tuples, which are lists of strings.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<List<String>> convertByteArrayto2DList(byte[] data) {
		String result = Arrays.toString(data);
		//Will be in array form, so this removes "[" and "]"
		result = result.substring(1, result.length()-1); 
		
		String[] resultArray = result.split(", ");
		
		result = "";
		for(String s : resultArray){
			result += (char)Integer.parseInt(s);
		}
		result = result.replaceAll("\\bNULL\\b","\"NULL\"");

		resultArray = result.split("\n");

		List<List<String>> resultList = new ArrayList<List<String>>();
		for(String s: resultArray){
			s = s.substring(1, s.length()-2); //remove new line character
			String[] columnArray = s.split("\",\"");
			for(int i = 0; i < columnArray.length; i++){
				
				if(columnArray[i].length() > 0){
					columnArray[i] = columnArray[i];
				}
			}
			
			resultList.add(new ArrayList(Arrays.asList(columnArray)));
		}
		
		return resultList;
	}
	
	/**
	 * 
	 * @param targetFile
	 * @param dataList
	 */
	public static void addStringListToTargetFile(String targetFile, List<String> dataList) {
		String INPUT_FILE = targetFile + ".txt";

		byte[] bytes = null;
		String inputString = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			inputString = byteToStringArray(bytes);

		} catch (IOException e) {
			//File does not exist, therefore create in next step
			inputString = "";
		}
		
		File entry;
		FileOutputStream fop = null;

		entry = new File(INPUT_FILE);
		try {
			fop = new FileOutputStream(entry);
			for(String dataEntry : dataList){
				inputString += dataEntry + "\n";
			}
			//Remove last newline character
			String outputString = inputString.substring(0, inputString.length()-1);
			
			byte[] data = outputString.getBytes();
			
			fop.write(data);
			fop.flush();
			fop.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<String> byteToStringList(String targetFile) throws Exception{
		byte[] bytes = null;
		String inputString = null;
		try (InputStream inputStream = new FileInputStream(targetFile)){
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			inputString = byteToStringArray(bytes);

		} catch (IOException e) {
			//File does not exist, therefore create in next step
			throw new Exception("Cannot find target file");
		}
		String[] stringArray = inputString.split("\n");
		ArrayList<String> stringList = new ArrayList(Arrays.asList(stringArray));
		
		return stringList;
	}
}
