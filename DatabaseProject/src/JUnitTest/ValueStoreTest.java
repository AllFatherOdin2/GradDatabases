package JUnitTest;
import java.io.File;
import java.util.Arrays;

import ProjectCode.ValueStoreException;
import ProjectCode.ValueStoreImpl;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import junit.framework.TestCase;


public class ValueStoreTest extends TestCase {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private static ValueStoreImpl valueStore;
	private static int intKey;
	private static byte[] data;
	
	@BeforeClass
    public void setUp() {
		valueStore = new ValueStoreImpl();
		data = new byte[2];
		data[0] = (byte)'a';
		data[1] = (byte)'c';
		
		intKey = 1;
    }
	
	@Test
	public void testPutDoesntThrowException() {
		File entry = new File(valueStore.getWorkingDirectory() + intKey + ".txt");
		
		if(entry.exists()){
			entry.delete();
		}
		
		valueStore.put(intKey, data);
	}
	
	@Test
	public void testGetGetsProperData() throws ValueStoreException{
		
		valueStore.put(intKey, data);
		
		byte[] gottenData = valueStore.get(intKey);
		assertNotNull(gottenData);
		assertEquals(Arrays.toString(data), Arrays.toString(gottenData));
	}
	
	@Test
	public void testPutOverwritesOldValues() throws ValueStoreException{
		valueStore = new ValueStoreImpl();
		
		valueStore.put(intKey, data);
		
		byte[] newData = data;
		newData[0] = (byte)'b';
		valueStore.put(intKey, newData);
		
		byte[] gottenData = valueStore.get(intKey);
		assertNotNull(gottenData);
		assertEquals(Arrays.toString(newData), Arrays.toString(gottenData));
	}

	@Test
	public void testGetNothingThrowsException() throws ValueStoreException{
		File entry = new File(valueStore.getWorkingDirectory() + intKey + ".txt");
		
		if(entry.exists()){
			entry.delete();
		}
		
		exception.expect(ValueStoreException.class);
		valueStore.get(intKey);
		
	}
}
