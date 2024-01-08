package closest;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Please implement a method (on a class called "ClosestToZeroFinder") with this
 * signature:
 * 
 * public int getClosestToZero(int[] input);
 * 
 * to:
 * 
 * -- return the number in the "input" array that is closest to zero.
 * 
 * -- If there are two equally close to zero elements like 2 and -2 - consider
 * the positive element to be "closer" to zero.
 * 
 * -- If the input is null or empty, throw an IllegalArgumentException.
 * 
 * Implement whatever other methods, and classes, you feel you need to be
 * expressive and keep the code extremely clean. Use Checkstyle to try to keep
 * the code even cleaner.
 */

public class WhenFindingClosestToZero {
	@Test
	public void findsZeroAmongMixedSign() {
		assertEquals(0, new ClosestToZeroFinder(new int[] { -3, 0, 5 }).getClosest());
		assertEquals(1, new ClosestToZeroFinder(new int[] { 1, 9, 5 }).getClosest());
		assertEquals(-1, new ClosestToZeroFinder(new int[] { -1, -9, 15 }).getClosest());
	}

	@Test
	public void prefersPositiveAmongTwoOfSameMagnitude() {
		assertEquals(1, new ClosestToZeroFinder(new int[] { 1, -1 }).getClosest());
		assertEquals(1, new ClosestToZeroFinder(new int[] { -1, 1 }).getClosest());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwIllegalArgumentException_IfInputArrayIsNull() throws Exception {
		new ClosestToZeroFinder(null).getClosest();

	}

	@Test(expected = IllegalArgumentException.class)
	public void throwIllegalArgumentException_IfInputArrayIsEmpty() throws Exception {
		new ClosestToZeroFinder(new int[] {}).getClosest();
	}
}
