package com.sangupta.outline;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.OutlineTestSupport.AllOptionCommand;
import com.sangupta.outline.OutlineTestSupport.RemoteAddCommand;
import com.sangupta.outline.help.OutlineHelp;

public class CommandOptionPopulationTest {

	@Test
	public void testBasics() {
		Outline outline = OutlineTestSupport.getOutline();
		
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
	}
	
	@Test
	public void testOptionPopulation() {
		Outline outline = OutlineTestSupport.getOutline();
		String[] args = "population-check -bo true -bo2 true -i 23 -i2 46 -d 29.94 -d2 49.94 -f 67.67 -f2 69.69 -l 123 -l2 1234 -short 12 -short2 24 -b 54 -b2 108 -c a -c2 x -s hello -stringArray sa1 sa2 sa3 sa4 sa5".split(" ");
		Object instance = outline.parse(args);

		Assert.assertNotNull(instance);
	    Assert.assertTrue(instance instanceof AllOptionCommand);
	    
	    AllOptionCommand aoc = (AllOptionCommand) instance;
	    
	    Assert.assertTrue(aoc.bool);
	    Assert.assertEquals(new Boolean(true), aoc.bool2);
	    
	    Assert.assertEquals(23, aoc.integer);
	    Assert.assertEquals(new Integer(46), aoc.integer2);
	    
	    Assert.assertEquals(29.94d, aoc.dbl, 0.0d);
	    Assert.assertEquals(new Double(49.94d), aoc.dbl2);
	    
	    Assert.assertEquals(67.67f, aoc.flt, 0.0f);
	    Assert.assertEquals(new Float(69.69), aoc.flt2);
	    
	    Assert.assertEquals(123l, aoc.lng);
	    Assert.assertEquals(new Long(1234l), aoc.lng2);
	    
	    Assert.assertEquals(12, aoc.shrt);
	    Assert.assertEquals(new Short((short) 24), aoc.shrt2);
	    
	    Assert.assertEquals(54, aoc.bite);
	    Assert.assertEquals(new Byte((byte) 108), aoc.bite2);
	    
	    Assert.assertEquals('a', aoc.chr);
	    Assert.assertEquals(new Character('x'), aoc.chr2);
	    
	    Assert.assertEquals("hello", aoc.strng);
	    
	    Assert.assertArrayEquals("sa1 sa2 sa3 sa4 sa5".split(" "), aoc.multiString);
	}
	
	@Test
	public void testMultiCommandForGroup() {
		Outline outline = OutlineTestSupport.getOutline();
		
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
	}

}
