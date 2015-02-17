/**
 * A custom exception class for our datastore implementation
 */

/**
 * @author JBosworth
 * @author DModica
 *
 */
public class valueStoreException extends Exception {
	public valueStoreException(){
		return super("The requested key either does not exist or could not be found in the datastore");
	}
	
	public valueStoreException(String msg){
		return super(msg);
	}
}
