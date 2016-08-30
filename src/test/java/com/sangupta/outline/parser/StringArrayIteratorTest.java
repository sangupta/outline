package com.sangupta.outline.parser;

import org.junit.Assert;
import org.junit.Test;

public class StringArrayIteratorTest {

	@Test
	public void test() {
		StringArrayIterator iterator = new StringArrayIterator(null);
		Assert.assertNotNull(iterator);
		Assert.assertFalse(iterator.hasNext());

		iterator = new StringArrayIterator(new String[] { });
		Assert.assertNotNull(iterator);
		Assert.assertFalse(iterator.hasNext());
		
		iterator = new StringArrayIterator(new String[] { "hello" });
		Assert.assertNotNull(iterator);
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals("hello", iterator.peek());
		Assert.assertEquals("hello", iterator.peek());
		Assert.assertEquals("hello", iterator.peek());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals("hello", iterator.next());
		Assert.assertFalse(iterator.hasNext());
		
		iterator = new StringArrayIterator(new String[] { "hello", "world" });
		Assert.assertNotNull(iterator);
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals("hello", iterator.peek());
		Assert.assertEquals("hello", iterator.peek());
		Assert.assertEquals("hello", iterator.peek());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals("hello", iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals("world", iterator.next());
		Assert.assertFalse(iterator.hasNext());
		
		iterator = new StringArrayIterator(new String[] { "hello", "world" });
		try {
			Assert.assertTrue(iterator.hasNext());
			iterator.remove();
			Assert.assertTrue(false);
		} catch(UnsupportedOperationException e) {
			Assert.assertTrue(true);
		}
	}
}
