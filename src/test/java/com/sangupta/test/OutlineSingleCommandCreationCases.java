package com.sangupta.test;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.outline.Outline;

public class OutlineSingleCommandCreationCases {
	
	@Test
	public void testExceptions() {
		Outline outline;
		
		try {
			outline = new Outline((Class<?>) null);
			
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		// setting null as default command
		try {
			outline = new Outline(PingCommandForTest.class).withDefaultCommand(null);
			
			Assert.assertTrue(false);
		} catch(IllegalStateException e) {
			Assert.assertTrue(true);
		}
		
		// adding null commands
		try {
			outline = new Outline(PingCommandForTest.class).withCommands(null);
			
			Assert.assertTrue(false);
		} catch(IllegalStateException e) {
			Assert.assertTrue(true);
		}
		
		// single command mode without any annotation
		try {
			outline = new Outline(PingCommandForTest.class).withCommands(null);
			
			Assert.assertTrue(false);
		} catch(IllegalStateException e) {
			Assert.assertTrue(true);
		}
		
		// single command and adding more commands
		try {
			outline = new Outline(PingCommandForTest.class).withCommands(null);
			
			Assert.assertTrue(false);
		} catch(IllegalStateException e) {
			Assert.assertTrue(true);
		}
	}

}
