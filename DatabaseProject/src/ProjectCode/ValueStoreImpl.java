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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.util.Arrays;

public class ValueStoreImpl {
	private static String direct = System.getProperty("user.dir") + "\\";
	
	public ValueStoreImpl(){}
	
	public ValueStoreImpl(String custDir){
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
	 */
	public void put(int key, byte[] data){
		File entry;
		FileOutputStream fop = null;
		try{
			entry = new File(direct + key +".txt");
			fop = new FileOutputStream(entry);
			
			if(!entry.exists()){
				//System.out.println("Building new entry");
				entry.createNewFile();
			}
			//else{ System.out.println("Overwritting old entry"); }
			
			fop.write(data);
			fop.flush();
			fop.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Will look for a file with the same name as the key, 
	 * and read the data from it, returning it as a byte[]
	 * 
	 * @param key File name to search for
	 * @return Data stored in the text file
	 * @throws ValueStoreException 
	 */
	public byte[] get(int key) throws ValueStoreException{
		final String INPUT_FILE = direct + key + ".txt";
		
		byte[] bytes = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			bytes = new byte[inputStream.available()];
			//System.out.println("Available bytes from the file: "+inputStream.available());
			
			int bytesread = inputStream.read(bytes);
			
			//System.out.println("Read Bytes: "+bytesread);
			//System.out.println(Arrays.toString(bytes));
			
		} catch(FileNotFoundException e){
			throw new ValueStoreException("No value exists at key: " + key);
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return bytes;
	}
	
	/**
	 * Deletes text file with the same name as the given key
	 * 
	 * @param key Name of file to delete
	 * @throws ValueStoreException 
	 */
	public void remove(int key) throws ValueStoreException{

		File entry = new File(direct + key + ".txt");
		
		if(entry.exists()){
			entry.delete();
			//System.out.println("Entry Deleted");
		}
		else{
			throw new ValueStoreException("No value exists at key: " + key);
		}
		
	}
}
