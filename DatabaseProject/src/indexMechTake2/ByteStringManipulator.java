package indexMechTake2;

import java.util.ArrayList;
import java.util.Arrays;
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
			
			List<String> keys = Arrays.asList(keyArray);
			
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
	
}
