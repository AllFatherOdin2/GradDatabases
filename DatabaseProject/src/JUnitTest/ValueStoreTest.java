package JUnitTest;
import java.io.File;

import ProjectCode.ValueStoreException;
import ProjectCode.ValueStoreImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import junit.framework.TestCase;


public class ValueStoreTest extends TestCase {
	private ValueStoreImpl valueStore;
	private int intKey;
	private byte[] data;
	
	@Test
	public void testPut() {
		valueStore = new ValueStoreImpl();
		data = new byte[2];
		data[0] = (byte)'a';
		data[1] = (byte)'c';
		
		intKey = 1;
		File entry = new File(valueStore.getWorkingDirectory() + intKey + ".txt");
		
		if(entry.exists()){
			entry.delete();
		}
		
		valueStore.put(1, data);
	}

}
