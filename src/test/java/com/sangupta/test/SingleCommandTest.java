package com.sangupta.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.Outline;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.exceptions.InvalidOutlineArgumentException;
import com.sangupta.outline.help.OutlineHelp;

public class SingleCommandTest {
	
	@Test
	public void testNoArguments() {
		String[] args = new String[] { };
        PingCommand ping = new Outline(PingCommand.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNull(ping.helpCommand);
	}
    
	@Test
    public void testArgumentsForHelp() {
    	String[] args = new String[] { "help" };
        PingCommand ping = new Outline(PingCommand.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
    }
	
	@Test
    public void testRandomNonHelpArgumentWithErrorCheckEnabled() {
    	String[] args = new String[] { "sangupta" };
        PingCommand ping = new Outline(PingCommand.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
    }

	@Test
    public void testRandomNonHelpArgumentWithErrorCheckDisabled() {
    	String[] args = new String[] { "sangupta" };
    	
    	try {
    		PingCommand ping = new Outline(PingCommand.class).withHelpOnIncorrectArguments(false).parse(args);
    		
    		// this must fail
    		Assert.assertTrue(false);
    	} catch(InvalidOutlineArgumentException e) {
    		Assert.assertTrue(true);
    		Assert.assertEquals("sangupta", e.argument);
    		Assert.assertEquals(0, e.positionIndex);
    	}
    }

    @Command(name = "ping", description = "Ping networks")
    public static class PingCommand {
    	
    	@Inject
    	public OutlineHelp helpCommand;
        
    	public void run() {
    		System.out.println("This is the ping command");
    	}
    }
    
}
