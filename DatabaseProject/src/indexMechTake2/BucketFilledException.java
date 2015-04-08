package indexMechTake2;

public class BucketFilledException extends Exception {
	/**
	 * Default constructor.
	 */
	public BucketFilledException(){
		super("The bucket is filled.");
	}
	
	/**
	 * Passes a custom message in for the exception
	 * 
	 * @param msg Custom message to return in exception
	 */
	public BucketFilledException(String msg){
		super(msg);
	}
}
