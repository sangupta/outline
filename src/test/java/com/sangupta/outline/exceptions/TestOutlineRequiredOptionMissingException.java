package com.sangupta.outline.exceptions;

import org.junit.Assert;
import org.junit.Test;

public class TestOutlineRequiredOptionMissingException {

	@Test
	public void test() {
		OutlineRequiredOptionMissingException ex = new OutlineRequiredOptionMissingException("hello");
		
		Assert.assertNotNull(ex);
		Assert.assertNotNull(ex.argument);
		Assert.assertEquals("hello", ex.argument);
		
		try {
			ex = new OutlineRequiredOptionMissingException(null);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}

		try {
			ex = new OutlineRequiredOptionMissingException("");
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}
	
}
