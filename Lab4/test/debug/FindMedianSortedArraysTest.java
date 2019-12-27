package debug;

import org.junit.Test;

import static org.junit.Assert.*;


public class FindMedianSortedArraysTest {
	
	@Test
	public void test1() {
		int[] a = new int[]{1, 3};
		int[] b = new int[]{2};
		assertEquals(2, new FindMedianSortedArrays().findMedianSortedArrays(a, b), 1e-2);
	}
	
	@Test
	public void test2() {
		int[] a = new int[]{1, 2};
		int[] b = new int[]{3, 4};
		assertEquals(2.5, new FindMedianSortedArrays().findMedianSortedArrays(a, b), 1e-2);
	}
	@Test
	public void test3() {
		int[] a = new int[]{1, 1, 1};
		int[] b = new int[]{5, 6, 7};
		assertEquals(3, new FindMedianSortedArrays().findMedianSortedArrays(a, b), 1e-2);
	}
	
	@Test
	public void test4() {
		int[] a = new int[]{1, 1};
		int[] b = new int[]{1, 2, 3};
		assertEquals(1, new FindMedianSortedArrays().findMedianSortedArrays(a, b), 1e-2);
	}
	
}
