package com.sangupta.test;

import com.sangupta.outline.Outline;
import com.sangupta.outline.annotations.Argument;
import com.sangupta.outline.annotations.Arguments;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.annotations.OptionType;
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
        
        args = "-g1 op1 -g2 op2 op3 remote -gr1 op4 -gr2 op5 op6 add -c1 op7 -c2 op8 op9 arg1 arg2 arg3 arg4".split(" ");
        
        Object instance = outline.parse(args);
        if(instance == null) {
            // do nothing
            return;
        }
        
        // execute the command
    }

    @Command(name = "add", description = "add command")
    public static class AddCommand {
        
        @Option(name = "-g1", type = OptionType.GLOBAL)
        private String g1;
        
        @Option(name = "-g2", type = OptionType.GLOBAL, arity = 2)
        private String[] g2;
        
    }
    
    @Command(name = "reset", description = "reset command")
    public static class ResetCommand {
        
    }
    
    @Command(group = "remote", name = "add", description = "remote add command")
    public static class RemoteAddCommand {
        
        @Option(name = "-gr1", type = OptionType.GROUP)
        private String gr1;
        
        @Option(name = "-gr2", type = OptionType.GROUP, arity = 2)
        private String[] gr2;
        
        @Option(name = "-c1")
        private String c1;
        
        @Option(name = "-c2", arity = 2)
        private String[] c2;
        
        @Argument(order = 0)
        private String a1;

        @Argument(order = 1)
        private String a2;
        
        @Arguments
        private String[] a3;
        
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
