import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class valueStoreImpl {
	public static void main(String[] args){
		final String INPUT_FILE = "C:\\Users\\Helios\\Desktop\\test.txt";
		
		
		try (InputStream inputStream = new FileInputStream(INPUT_FILE)){
			byte[] bytes = new byte[inputStream.available()];
			System.out.println("Available bytes from the file: "+inputStream.available());
			
			int bytesread = inputStream.read(bytes);
			
			System.out.println("Read Bytes: "+bytesread);
			System.out.println(Arrays.toString(bytes));
			
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Will read in data from a byte array, and build a text file containing the data, with the key as the name of the file
	 * 
	 * @param key Key to write data under
	 * @param data Data to record
	 */
	public void put(int key, byte[] data){
		
	}
	
	/**
	 * Will look for a file with the same name as the key, 
	 * and read the data from it, returning it as a byte[]
	 * 
	 * @param key File name to search for
	 * @return Data stored in the text file
	 */
	public byte[] get(int key){
	
		
		return null;
	}
	
	/**
	 * Deletes text file with the same name as the given key
	 * 
	 * @param key Name of file to delete
	 */
	public void remove(int key){
		
	}
	
	private 
}