package undoRedoLogging;

import java.util.ArrayList;
import java.util.List;

public class Block {
	private List<Tuple> tuples;
	
	/**
	 * block constructor for an empty list of tuples
	 */
	public Block(){
		tuples = new ArrayList<Tuple>();
	}
	
	/**
	 * Block constructor for a list of tuples
	 * @param tuples
	 */
	public Block(List<Tuple> tuples){
		this.tuples = tuples;
	}
	
	/**
	 * adds a single tuple to this block.
	 * @param tuple
	 */
	public void addTuple(Tuple tuple){
		tuples.add(tuple);
	}
	
	/**
	 * getter for the list of tuples
	 * @return list of tuples
	 */
	public List<Tuple> getTuples(){
		return tuples;
	}
	
}
