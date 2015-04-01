package JUnitTest;

import java.util.List;

import org.junit.Test;

import ProjectCode.QueryExecException;
import ProjectCode.QueryExecImpl;
import junit.framework.TestCase;

public class QueryExecTest extends TestCase {
	
	@Test
	public void testRunOpenNextClose() throws QueryExecException{
		QueryExecImpl queryExec = new QueryExecImpl();
		List<String> results = queryExec.open();
		
		assertEquals(results.size(), 18);
		assertTrue(queryExec.getQueryTime() > 0);
		System.out.println("Elapsed Query Time: " + queryExec.getQueryTime());
	}
}
