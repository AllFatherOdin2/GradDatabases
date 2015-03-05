package JUnitTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import ProjectCode.indexMechException;
import ProjectCode.indexMechImpl;

import org.junit.BeforeClass;
import org.junit.Test;

public class indexMechTest {
	private static indexMechImpl indexMech;

	@BeforeClass
	public static void setup(){
		indexMech = new indexMechImpl();
	}
	
	
	@Test
	public void testPickedDirectory(){
		String dirTest = System.getProperty("user.dir");
		indexMech = new indexMechImpl(dirTest);
		
		assertEquals(indexMech.getWorkingDirectory(), dirTest + "\\");
		indexMech = new indexMechImpl();
		assertNotSame(indexMech.getWorkingDirectory(), dirTest + "\\");
	}
	
	@Test
	public void testPutAndGet() throws indexMechException {
		indexMech.put("1", "Hello");
		List<String> result = indexMech.get("Hello");
		
		assertEquals(result.size(), 1);
		assertEquals(result.get(0), "1");
		indexMech.remove("Hello");
	}
	
	@Test
	public void testDoublePutThrowsError() throws indexMechException {
		String errorMessage = "";
		try{
			indexMech.put("1", "doubleInput");
			indexMech.put("1", "doubleInput");
		}catch(indexMechException e){
			errorMessage = e.getMessage();
		}
		indexMech.remove("doubleInput");
		assertEquals("The Datavalue already indexes this key", errorMessage);
	}

	@Test
	public void testPutGetTwiceSameKey() throws indexMechException {

		indexMech.put("1", "Hello");
		indexMech.put("1", "World");
		
		List<String> result = indexMech.get("Hello");
		assertEquals(result.size(), 1);
		assertEquals(result.get(0), "1");
		
		result = indexMech.get("World");
		assertEquals(result.size(), 1);
		assertEquals(result.get(0), "1");
		indexMech.remove("Hello");
		indexMech.remove("World");
	}
	
	@Test
	public void testPutGetTwiceSameDataValue() throws indexMechException {

		indexMech.put("1", "Hello");
		indexMech.put("2", "Hello");
		
		List<String> result = indexMech.get("Hello");
		assertEquals(result.size(), 2);
		assertTrue(result.contains("1"));
		assertTrue(result.contains("2"));
		indexMech.remove("Hello");
	}
	
	@Test
	public void testRemoveTwiceThrowsError(){
		String errorMessage = "";
		try{
			indexMech.put("1", "removeTwice");
			indexMech.remove("removeTwice");
			indexMech.remove("removeTwice");
		}catch(indexMechException e){
			errorMessage = e.getMessage();
		}
		assertEquals("No index file could be found", errorMessage);
	}
	
	@Test
	public void testGetWhatsNotThere(){
		String errorMessage = "";
		try{
			indexMech.get("notThere");
		}catch(indexMechException e){
			errorMessage = e.getMessage();
		}
		assertEquals("No index file could be found", errorMessage);
	}
}
