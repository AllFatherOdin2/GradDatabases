package JUnitTest;
import ProjectCode.ValueStoreImpl;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;


public class ValueStoreTest extends TestCase {
	@Before
	public void initialize(){
		ValueStoreImpl valueStore = new ValueStoreImpl();
	}

	@Test
	public void test() {
		fail("This test failed");
	}

}
