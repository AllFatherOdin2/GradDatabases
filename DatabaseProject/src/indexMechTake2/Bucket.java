package indexMechTake2;

import java.util.List;

public class Bucket {
	int MAX_BUCKET_SIZE = 10;
	private static final String OVERFLOW_TITLE = "overflow";
	
	private String index; 			//Already hashed value
	private List<String> keys; 		//All keys in the index held in this bucket
	private boolean hasOverflowed; 	//tracking if there is another bucket of the same index.
	
	/**
	 * Basic data structure for use in index.
	 * 
	 * @param index Identifier of the bucket
	 * @param keys List of keys stored in the bucket.
	 */
	public Bucket(String index, List<String> keys){
		this.setIndex(index);
		if(index.equals(OVERFLOW_TITLE)){
			this.setToOverflow();
		}
		this.setKeys(keys);
		if(keys.size() >= MAX_BUCKET_SIZE)
			this.setHasOverflowed(true);
		else
			this.setHasOverflowed(false);
	}
	
	/**
	 * Takes a key and adds it to the bucket. Should never be reached by a key beloning to a different bucket.
	 * 
	 * @param key Key to add
	 * @return List of keys in the bucket, including new key.
	 * @throws BucketOverflowException
	 * @throws BucketFilledException
	 */
	public List<String> addKey(String key) throws BucketOverflowException, BucketFilledException{
		
		if(this.hasOverflowed){
			//If this triggers, then another bucket with the same index already exists,
			//therefore calling function should just ignore this one and find the other.
			throw new BucketFilledException();
		}
		
		
		if(keys.size() < MAX_BUCKET_SIZE){
			keys.add(key);
			
			return keys;
		}

		//Catch on other end to create overflow bucket
		this.setHasOverflowed(true);
		throw new BucketOverflowException();
	}
	
	
	/**
	 * Checks if the bucket is the correct one to put a key in.
	 * @param hashedValue Datavalue that has been hashed into a potential index value.
	 * @return True if the index of the bucket matches the hashed data value, else false.
	 */
	public boolean isCorrectBucket(String hashedValue){
		return hashedValue.equals(this.getIndex());
	}
	
	/**
	 * Removes a single key from a bucket.
	 * 
	 * @param key Key to remove
	 * @return List of keys remaining in bucket, minus the removed one.
	 */
	public List<String> removeKeys(String key){
		for(int x = 0; x < keys.size(); x++){
			if(keys.get(x).equals(key)){
				keys.remove(x);
				x--;
			}
		}
		
		if(keys.size() < MAX_BUCKET_SIZE){
			//this bucket is no longer overflowing, and can accept more.
			this.setHasOverflowed(false);
		}
		this.setKeys(keys);
		return keys;
	}
	
	/**
	 * Overwrites default toString() method for Objects
	 */
	public String toString(){
		String str = this.getIndex() + "`";
		
		for(String key : this.getKeys()){
			str += key + "~";
		}
		
		return str.substring(0, str.length()-1);
	}
	
	/**
	 * Makes sure that the maximum bucket size of the overflow bucket is essentially unlimited size.
	 */
	private void setToOverflow(){
		this.MAX_BUCKET_SIZE = Integer.MAX_VALUE;
	}
	
	/**
	 * Getter method for index
	 * 
	 * @return The index of the bucket
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * Setter method for index
	 * @param index Index to set for the bucket
	 */
	private void setIndex(String index) {
		this.index = index;
	}
	
	/**
	 * Getter method to return the list of keys
	 * @return List of keys stored in the bucket
	 */
	public List<String> getKeys() {
		return keys;
	}
	/**
	 * Setter method to put a new list of keys in a bucket
	 * @param keys List of keys to be stored in bucket
	 */
	private void setKeys(List<String> keys) {
		this.keys = keys;
	}

	/**
	 * Getter method to determine if the bucket is overflowing
	 * @return True if the bucket has reached max size, else false
	 */
	public boolean isHasOverflowed() {
		return hasOverflowed;
	}
	/**
	 * Setter method to change if the bucket is overflowing
	 * @param hasOverflowed Boolean deciding if you want the bucket to be overflowing
	 */
	private void setHasOverflowed(boolean hasOverflowed) {
		this.hasOverflowed = hasOverflowed;
	}

}
