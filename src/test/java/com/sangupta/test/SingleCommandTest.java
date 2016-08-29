package com.sangupta.test;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.Outline;
import com.sangupta.outline.exceptions.InvalidOutlineArgumentException;

public class SingleCommandTest {
	
	@Test
	public void testNoArguments() {
		String[] args = new String[] { };
		PingCommandForTest ping = new Outline(PingCommandForTest.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNull(ping.helpCommand);
	}
    
	@Test
    public void testArgumentsForHelp() {
    	String[] args = new String[] { "help" };
    	PingCommandForTest ping = new Outline(PingCommandForTest.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
    }
	
	@Test
    public void testRandomNonHelpArgumentWithErrorCheckEnabled() {
    	String[] args = new String[] { "sangupta" };
    	PingCommandForTest ping = new Outline(PingCommandForTest.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
    }
	
	@Test
    public void testRandomNonHelpArgumentWithErrorCheckDisabled() {
    	String[] args = new String[] { "sangupta" };
    	
    	try {
    		PingCommandForTest ping = new Outline(PingCommandForTest.class).withHelpOnIncorrectArguments(false).parse(args);
    		
    		// this must fail
    		Assert.assertTrue(false);
    	} catch(InvalidOutlineArgumentException e) {
    		Assert.assertTrue(true);
    		Assert.assertEquals("sangupta", e.argument);
    		Assert.assertEquals(0, e.positionIndex);
    	}
    }

	@Test
	public void testHelpDisplay() {
		String[] args = new String[] { "help" };
		PingCommandForTest ping = new Outline(PingCommandForTest.class).withHelpOnIncorrectArguments(true).parse(args);
		
		Assert.assertTrue(true);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
        
        ping.helpCommand.showHelpIfRequested();
	}
}
