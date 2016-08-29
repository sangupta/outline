package com.sangupta.test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.util.StringUtils;
import com.sangupta.outline.Outline;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.annotations.OptionType;
import com.sangupta.outline.exceptions.InvalidOutlineConfigurationException;
import com.sangupta.outline.help.OutlineHelp;

public class OutlineSingleCommandCreationCases {
	
	@Test
	public void testExceptions() {
		Outline outline = null;
		
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
		
		// single command with group option
		try {
			outline = new Outline(PingCommandWithGroupOption.class).parse(StringUtils.EMPTY_STRING_LIST);
			
			Assert.assertTrue(false);
		} catch(InvalidOutlineConfigurationException e) {
			Assert.assertTrue(true);
			
			Assert.assertEquals("ping", e.className);
			Assert.assertEquals("gr1", e.field);
		}
		
		// this must be null - as all are error cases above
		Assert.assertNull(outline);
	}

	@Command(name = "ping", description = "Ping networks")
	public static class PingCommandWithGroupOption {
		
		@Inject
		public OutlineHelp helpCommand;
		
		@Option(name = "--global", description = "This is some description for the global flag", type = OptionType.GLOBAL, arity = 0)
		protected String g;
	    
	    @Option(name = "-gr1", type = OptionType.GROUP)
	    protected String gr1;
	    
	}
	
}
