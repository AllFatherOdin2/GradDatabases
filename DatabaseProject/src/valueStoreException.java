/**
 * A custom exception class for our datastore implementation
 */

/**
 * @author JBosworth
 * @author DModica
 *
 */
public class valueStoreException extends Exception {
	/**
	 * Default constructor.
	 */
	public valueStoreException(){
		super("The requested key either does not exist or could not be found in the datastore");
	}
	
	/**
	 * Passes a custom message in for the exception
	 * 
	 * @param msg Custom message to return in exception
	 */
	public valueStoreException(String msg){
		super(msg);
	}
}
