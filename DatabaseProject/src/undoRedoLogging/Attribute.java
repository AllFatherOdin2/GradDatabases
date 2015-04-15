package undoRedoLogging;

public class Attribute {
	private String name;
	
	/**
	 * Basic Constructor
	 * @param name Name of the attribute
	 */
	public Attribute(String name){
		this.name = name;
	}
	
	/**
	 * @return the key
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the key to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
