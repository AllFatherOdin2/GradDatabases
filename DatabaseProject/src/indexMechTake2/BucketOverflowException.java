package indexMechTake2;

public class BucketOverflowException extends Exception {
	/**
	 * Default constructor.
	 */
	public BucketOverflowException(){
		super("The bucket has overflowed.");
	}
	
	/**
	 * Passes a custom message in for the exception
	 * 
	 * @param msg Custom message to return in exception
	 */
	public BucketOverflowException(String msg){
		super(msg);
	}
}
