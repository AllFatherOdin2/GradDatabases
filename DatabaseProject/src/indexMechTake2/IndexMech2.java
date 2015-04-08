package indexMechTake2;
/**
 * A valuestore implementation using multiple files for storage. 
 * File size is completely dynamic, and is based on the size of the input data
 */

/**
 * @author JBosworth
 * @author DModica
 *
 */

public class IndexMech2 {
	private static String direct = System.getProperty("user.dir") + "\\";
	
	public IndexMech2(){}
	
	public IndexMech2(String custDir){
		direct = custDir + "\\";
	}
	
	/**
	 * Getter Function
	 * 
	 * @return the current working directory
	 */
	public String getWorkingDirectory(){
		return direct;
	}

}
