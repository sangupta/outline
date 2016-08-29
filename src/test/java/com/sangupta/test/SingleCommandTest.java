package com.sangupta.test;

import javax.inject.Inject;

import com.sangupta.outline.Outline;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.help.OutlineHelp;

public class SingleCommandTest {
    
    public static void main(String[] args) {
    	args = new String[] { "help" };
        PingCommand ping = new Outline(PingCommand.class).parse(args);
        boolean exit = ping.helpCommand.showHelpIfRequested();
        if(exit) {
        	return;
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
