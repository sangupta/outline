package com.sangupta.outline;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.Outline;
import com.sangupta.outline.help.OutlineHelp;
import com.sangupta.outline.parser.ParseResult;

/**
 * Tests related to where we test only with a group argument.
 * 
 * @author sangupta
 *
 */
public class GroupCommandTest {

	@Test
	public void testGroupNoCommand() {
		Outline outline = OutlineTestSupport.getOutline();
		
		String[] args = "help remote".split(" ");
		Object instance = outline.parse(args);
		
		Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof OutlineHelp);
	    
	    OutlineHelp help = (OutlineHelp) instance;
	    ParseResult result = help.getResult();
	    Assert.assertNotNull(result);
	    
	    Assert.assertEquals(null, result.command);
	    Assert.assertEquals("remote", result.group);
	}
	
	@Test
	public void testGroupInvalidCommand() {
		Outline outline = OutlineTestSupport.getOutline();
		
		String[] args = "help sangupta".split(" ");
		Object instance = outline.parse(args);
		
		Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof OutlineHelp);
	    
	    OutlineHelp help = (OutlineHelp) instance;
	    ParseResult result = help.getResult();
	    Assert.assertNotNull(result);
	    
	    Assert.assertEquals("sangupta", result.command);
	    Assert.assertEquals(null, result.group);
	}
	
	@Test
	public void testGroupCorrectGroupInvalidCommand() {
		Outline outline = OutlineTestSupport.getOutline();
		
		String[] args = "help remote sangupta".split(" ");
		Object instance = outline.parse(args);
		
		Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof OutlineHelp);
	    
	    OutlineHelp help = (OutlineHelp) instance;
	    ParseResult result = help.getResult();
	    Assert.assertNotNull(result);
	    
	    Assert.assertEquals("sangupta", result.command);
	    Assert.assertEquals("remote", result.group);
	}

	@Test
	public void testGroupWithCommand() {
		Outline outline = OutlineTestSupport.getOutline();
		
		String[] args = "help remote radd".split(" ");
		Object instance = outline.parse(args);
		
		Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof OutlineHelp);
	    
	    OutlineHelp help = (OutlineHelp) instance;
	    ParseResult result = help.getResult();
	    Assert.assertNotNull(result);
	    
	    Assert.assertEquals("radd", result.command);
	    Assert.assertEquals("remote", result.group);
	}
	
	public static void main(String[] args) {
		Outline outline = OutlineTestSupport.getOutline();
		
		args = "help remote remote-add".split(" ");
		Object instance = outline.parse(args);
		
		((OutlineHelp) instance).showHelpIfRequested();
	}
}
