package undoRedoLogging;

import java.util.ArrayList;
import java.util.List;

public class Block {
	private List<Tuple> tuples;
	
	public Block(){
		tuples = new ArrayList<Tuple>();
	}
	
	public Block(List<Tuple> tuples){
		this.tuples = tuples;
	}
	
	public void addTuple(Tuple tuple){
		tuples.add(tuple);
	}
	
	public List<Tuple> getTuples(){
		return tuples;
	}
	
}
