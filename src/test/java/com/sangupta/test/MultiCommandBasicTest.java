package com.sangupta.test;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.Outline;
import com.sangupta.outline.help.OutlineHelp;
import com.sangupta.test.MultiCommandSupport.RemoteAddCommand;

public class MultiCommandBasicTest {

	@Test
	public void testBasics() {
		Outline outline = MultiCommandSupport.getOutline();
		
		// test that the instance is perfectly populated
	    String[] args = new String[] { "" };
	    Object instance = outline.parse(args);
	    
	    Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof OutlineHelp);
	    
	    // test for a specific command
	    args = "-g1 op1 -g2 op2 op3 remote -gr1 op4 -gr2 op5 op6 remote-add -c1 op7 -c2 op8 op9 arg1 arg2 arg3 arg4".split(" ");
	    instance = outline.parse(args);
	    
	    Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof RemoteAddCommand);
	    RemoteAddCommand command = (RemoteAddCommand) instance;
	    Assert.assertEquals(command.g1, "op1");
	    Assert.assertArrayEquals(command.g2, new String[] { "op2", "op3" });
	    Assert.assertEquals(command.gr1, "op4");
	    Assert.assertArrayEquals(command.gr2, new String[] { "op5", "op6" });
	    Assert.assertEquals(command.c1, "op7");
	    Assert.assertArrayEquals(command.c2, new String[] { "op8", "op9" });
	    Assert.assertEquals(command.a1, "arg1");
	    Assert.assertEquals(command.a2, "arg2");
	    Assert.assertArrayEquals(command.a3, new String[] { "arg3", "arg4" });
	    
	    // test for the help mode
	    args = new String[] { "help" };
	    instance = outline.parse(args);
	    
	    Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof OutlineHelp);
//	    ((OutlineHelp) instance).showHelp();
	}
	
	@Test
	public void testMultiCommandForGroup() {
		Outline outline = MultiCommandSupport.getOutline();
		
		// test that the instance is perfectly populated
		String[] args = new String[] { "" };
		Object instance = outline.parse(args);
		
		Assert.assertNotNull(instance);
		Assert.assertTrue(instance instanceof OutlineHelp);
		
		// test for a specific command
		args = "help remote".split(" ");
		instance = outline.parse(args);
		
		Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof OutlineHelp);
//	    ((OutlineHelp) instance).showHelp();
	}

}
