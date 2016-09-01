package com.sangupta.outline;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.exceptions.OutlineInvalidArgumentException;

public class SingleCommandTest {
	
	@Test
	public void testNoArguments() {
		String[] args = new String[] { };
		PingCommandSupport ping = new Outline(PingCommandSupport.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNotNull(ping.helpCommand);
	}
    
	@Test
    public void testArgumentsForHelp() {
    	String[] args = new String[] { "help" };
    	PingCommandSupport ping = new Outline(PingCommandSupport.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
    }
	
	@Test
    public void testRandomNonHelpArgumentWithErrorCheckEnabled() {
    	String[] args = new String[] { "sangupta" };
    	PingCommandSupport ping = new Outline(PingCommandSupport.class).parse(args);
        
        Assert.assertNotNull(ping);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
    }
	
	@Test
    public void testRandomNonHelpArgumentWithErrorCheckDisabled() {
    	String[] args = new String[] { "sangupta" };
    	
    	try {
    		PingCommandSupport ping = new Outline(PingCommandSupport.class).withHelpOnIncorrectArguments(false).parse(args);
    		
    		// this must fail
    		Assert.assertTrue(false);
    	} catch(OutlineInvalidArgumentException e) {
    		Assert.assertTrue(true);
    		Assert.assertEquals("sangupta", e.argument);
    		Assert.assertEquals(0, e.positionIndex);
    	}
    }

	@Test
	public void testHelpDisplay() throws IOException {
		String[] args = new String[] { "help" };
		PingCommandSupport ping = new Outline(PingCommandSupport.class).withHelpOnIncorrectArguments(true).parse(args);
		
		Assert.assertTrue(true);
        Assert.assertNotNull(ping.helpCommand);
        Assert.assertTrue(ping.helpCommand.isHelpRequested());
        
//        ping.helpCommand.showHelpIfRequested();
        
//        File file = new File(getClass().getClassLoader().getResource("ping-help.txt").getFile());
//        String expected = FileUtils.readFileToString(file).trim();
//        String actual = ping.helpCommand.getHelp().trim();
//        Assert.assertEquals(expected, actual);
	}
	
	public static void main(String[] args) {
		args = new String[] { "help" };
		PingCommandSupport ping = new Outline(PingCommandSupport.class).withHelpOnIncorrectArguments(true).parse(args);
		ping.helpCommand.showHelpIfRequested();
	}
}
