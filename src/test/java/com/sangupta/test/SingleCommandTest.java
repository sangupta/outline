package com.sangupta.test;

import com.sangupta.outline.Outline;
import com.sangupta.outline.annotations.Command;

public class SingleCommandTest {
    
    public static void main(String[] args) {
    	args = new String[] { "help" };
        PingCommand ping = new Outline(PingCommand.class).parse(args, PingCommand.class);
        System.out.println(ping);
    }

    @Command(name = "ping", description = "Ping networks")
    public static class PingCommand {
        
    }
    
}
