package com.sangupta.outline.util;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.util.OutlineUtil;

public class OutlineUtilTest {
	
	@Test
	public void testJoin() {
		Assert.assertEquals("", OutlineUtil.join(null, ", "));
		Assert.assertEquals("", OutlineUtil.join("".split(" "), ", "));
		Assert.assertEquals("hello", OutlineUtil.join("hello".split(" "), ", "));
		Assert.assertEquals("hello, world", OutlineUtil.join("hello:world".split(":"), ", "));
		
		Assert.assertEquals("helloworld", OutlineUtil.join("hello:world".split(":"), null));
	}

}
