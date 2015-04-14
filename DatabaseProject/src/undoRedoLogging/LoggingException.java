/**
 * A custom exception class for our datastore implementation
 */
package undoRedoLogging;

/**
 * @author DModica
 * @author JBosworth
 */
public class LoggingException extends Exception{
	/**
	 * Default constructor.
	 */
	public LoggingException(){
		super("An error occured in the log");
	}
	
	/**
	 * Passes a custom message in for the exception
	 * 
	 * @param msg Custom message to return in exception
	 */
	public LoggingException(String msg){
		super(msg);
	}
}
