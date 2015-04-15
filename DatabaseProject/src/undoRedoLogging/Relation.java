package undoRedoLogging;

import java.util.ArrayList;
import java.util.List;

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
	
	public void addTuples(String url){
		
	}
}
