package com.sangupta.test;

import com.sangupta.outline.Outline;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.cmdfactory.DefaultCommandFactory;

public class IntegrationTest {
    
    public static void main(String[] args) {
        Outline outline = new Outline("git")
                                    .withDescription("the powerful SCM tool")
                                    .withDefaultCommand(AddCommand.class)
                                    .withHelpKeyword("help")
                                    .withCommandFactory(new DefaultCommandFactory())
                                    .withCommands(AddCommand.class, ResetCommand.class)
                                    .withCommands(RemoteAddCommand.class, RemoteRemoveCommand.class);
        
        outline.withGroup("mygroup")
               .withDescription("some stupid group")
               .withCommands(GroupAddCommand.class, GroupRemoveCommand.class);
        
        Object instance = outline.parse(args);
        if(instance == null) {
            // do nothing
            return;
        }
        
        // execute the command
    }

    @Command(name = "add", description = "add command")
    public static class AddCommand {
        
    }
    
    @Command(name = "reset", description = "reset command")
    public static class ResetCommand {
        
    }
    
    @Command(group = "remote", name = "add", description = "remote add command")
    public static class RemoteAddCommand {
        
    }
    
    @Command(group = "remote", name = "remove", description = "remote remove command")
    public static class RemoteRemoveCommand {
        
    }
    
    @Command(name = "add", description = "remote add command")
    public static class GroupAddCommand {
        
    }
    
    @Command(name = "remove", description = "remote remove command")
    public static class GroupRemoveCommand {
        
    }
}
