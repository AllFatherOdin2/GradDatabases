package ProjectCode;
/**
 * A custom exception class for our datastore implementation
 */

/**
 * @author JBosworth
 * @author DModica
 *
 */
public class ValueStoreException extends Exception {
	/**
	 * Default constructor.
	 */
	public ValueStoreException(){
		super("The requested key either does not exist or could not be found in the datastore");
	}
	
	/**
	 * Passes a custom message in for the exception
	 * 
	 * @param msg Custom message to return in exception
	 */
	public ValueStoreException(String msg){
		super(msg);
	}
}
