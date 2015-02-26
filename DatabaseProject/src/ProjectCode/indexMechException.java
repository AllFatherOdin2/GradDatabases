package ProjectCode;

/**
 * @author DModica
 * @author JBosworth
 *
 *Custom Exception class to handle errors from our Indexing Mechanism
 */
public class indexMechException extends Exception {
	/**
	 * Default constructor.
	 */
	public indexMechException(){
		super("The requested key either does not exist or could not be found in the datastore");
	}
	
	/**
	 * Passes a custom message in for the exception
	 * 
	 * @param msg Custom message to return in exception
	 */
	public indexMechException(String msg){
		super(msg);
	}
}
