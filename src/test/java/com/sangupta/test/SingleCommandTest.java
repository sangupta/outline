package com.sangupta.test;

import javax.inject.Inject;

import com.sangupta.outline.Outline;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.help.OutlineHelpCommand;

public class SingleCommandTest {
    
    public static void main(String[] args) {
    	args = new String[] { "help" };
        PingCommand ping = new Outline(PingCommand.class).parse(args);
        ping.helpCommand.showHelp();
    }

    @Command(name = "ping", description = "Ping networks")
    public static class PingCommand {
    	
    	@Inject
    	public OutlineHelpCommand helpCommand;
        
    }
    
}
