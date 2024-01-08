package model.patterns;

import junit.framework.TestCase;

public class IndexListTests extends TestCase {

	public void testToStringReportsCorridor() throws Exception {
		DirectionalCorridors list = new DirectionalCorridors();
		list.add(45);
		assertEquals("45 ", list.toString());
	}
}
