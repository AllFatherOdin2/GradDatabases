package indexMechTake2;

import java.util.List;

public class Bucket {
	int MAX_BUCKET_SIZE = 10;
	
	private String index; 			//Already hashed value
	private List<String> keys; 		//All keys in the index held in this bucket
	private boolean hasOverflowed; 	//tracking if there is another bucket of the same index.
	
	public Bucket(String index, List<String> keys){
		this.setIndex(index);
		this.setKeys(keys);
		if(keys.size() >= MAX_BUCKET_SIZE)
			this.setHasOverflowed(true);
		else
			this.setHasOverflowed(false);
	}
	
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
	
	public boolean isCorrectBucket(String hashedValue){
		return hashedValue.equals(this.getIndex());
	}
	
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
	
	public String toString(){
		String str = this.getIndex() + "`";
		
		for(String key : this.getKeys()){
			str += key + "~";
		}
		
		return str.substring(0, str.length()-1);
	}
	
	
	public void setToOverflow(){
		this.MAX_BUCKET_SIZE = Integer.MAX_VALUE;
	}
	
	public String getIndex() {
		return index;
	}
	private void setIndex(String index) {
		this.index = index;
	}
	
	public List<String> getKeys() {
		return keys;
	}
	private void setKeys(List<String> keys) {
		this.keys = keys;
	}


	public boolean isHasOverflowed() {
		return hasOverflowed;
	}


	private void setHasOverflowed(boolean hasOverflowed) {
		this.hasOverflowed = hasOverflowed;
	}

}
