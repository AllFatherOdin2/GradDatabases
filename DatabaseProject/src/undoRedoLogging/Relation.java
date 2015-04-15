package undoRedoLogging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import util.ByteStringManipulator;

/**
 * @author DModica
 * @author JBosworth
 */
public class Relation {
	private String name;
	private List<String> attributes;
	private List<Tuple> tuples;

	public Relation(String name, List<String> attributes){
		this.name = name;
		this.attributes = attributes;
		this.tuples = new ArrayList<Tuple>();
	}
	
	public void addTuples(String url) throws LoggingException{
		byte[] relationBytes = null;
		try (InputStream inputStream = new FileInputStream(url)){
			relationBytes = new byte[inputStream.available()];
			inputStream.read(relationBytes);
			convertByteToTuples(relationBytes);
			
			if(tuples.size() == 0){
				throw new LoggingException("That data table is empty");
			}
			
		} catch(IOException e){
			throw new LoggingException("No CSV could be found could be found at given URL");
		}
	}
	
	/**
	 * Takes a byte stream and creates a list of tuples to put in the Relation
	 * @param data Byte stream read in from file
	 */
	private void convertByteToTuples(byte[] data){
		List<List<String>> twoDStrings = ByteStringManipulator.convertByteArrayto2DList(data);
		
		for(List<String> stringList : twoDStrings){
			tuples.add(new Tuple(stringList));
		}	
	}
	
	public String getName(){
		return name;
	}

	public List<String> getAttributes(){
		return attributes;
	}
	
	public List<Tuple> getTuples(){
		return tuples;
	}
}
