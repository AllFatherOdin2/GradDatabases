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
	private List<Block> blocks;

	public Relation(String name, List<String> attributes){
		this.name = name;
		this.attributes = attributes;
		this.blocks = new ArrayList<Block>();
	}
	
	public void loadTuples(String url) throws LoggingException{
		byte[] relationBytes = null;
		try (InputStream inputStream = new FileInputStream(url)){
			relationBytes = new byte[inputStream.available()];
			inputStream.read(relationBytes);
			convertByteToBlocks(relationBytes);
			
			if(blocks.size() == 0){
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
	private void convertByteToBlocks(byte[] data){
		List<List<String>> twoDStrings = ByteStringManipulator.convertByteArrayto2DList(data);
		List<Tuple> tuples = new ArrayList<Tuple>();
		
		for(List<String> stringList : twoDStrings){
			tuples.add(new Tuple(stringList));
		}
		
		Block newBlock = new Block();
		
		int x = 0;
		for(Tuple tuple : tuples){
			newBlock.addTuple(tuple);
			x++;
			if(x >= 199){
				blocks.add(newBlock);
				newBlock = new Block();
				x = 0;
			}
		}
		
		if(x != 0){
			blocks.add(newBlock);
		}
		
	}
	
	
	public String getName(){
		return name;
	}

	public List<String> getAttributes(){
		return attributes;
	}
	
	public List<Block> getBlocks(){
		return blocks;
	}
}
