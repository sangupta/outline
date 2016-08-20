/**
 *
 * outline - command line argument parser
 * Copyright (c) 2015-2016, Sandeep Gupta
 * 
 * http://sangupta.com/projects/outline
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
 
package com.sangupta.test;

import java.lang.reflect.Field;
import java.util.List;

import com.sangupta.outline.Outline;
import com.sangupta.outline.OutlineTypeConverter;
import com.sangupta.outline.annotations.Argument;
import com.sangupta.outline.annotations.Arguments;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.annotations.OptionType;
import com.sangupta.outline.cmdfactory.DefaultCommandFactory;
import com.sangupta.outline.help.OutlineHelpCommand;

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
        
        args = "-g1 op1 -g2 op2 op3 remote -gr1 op4 -gr2 op5 op6 radd -c1 op7 -c2 op8 op9 arg1 arg2 arg3 arg4".split(" ");
        
        Outline.registerTypeConverter(String[].class, new OutlineTypeConverter<String[]>() {

            @Override
            public String[] convertFrom(Field field, Object instance, Object value) {
                if(value == null) {
                    return null;
                }
                
                if(value instanceof List<?>) {
                    List<?> list = (List<?>) value;
                    String[] array = new String[list.size()];
                    for(int index = 0; index < array.length; index++) {
                        array[index] = list.get(index).toString();
                    }
                    
                    return array;
                }
                
                return new String[] { value.toString() };
            }
            
        });
        
        Object instance = outline.parse(args);

        if(instance instanceof OutlineHelpCommand) {
            ((OutlineHelpCommand) instance).showHelp();
            return;
        }
        
        // execute the command
    }
    
    public static class GlobalCommand {
        
        @Option(name = "-g1", type = OptionType.GLOBAL)
        protected String g1;
        
        @Option(name = "-g2", type = OptionType.GLOBAL, arity = 2)
        protected String[] g2;
        
    }

    @Command(name = "add", description = "add command")
    public static class AddCommand extends GlobalCommand {
        
    }
    
    @Command(name = "reset", description = "reset command")
    public static class ResetCommand extends GlobalCommand {
        
    }
    
    public static class RemoteCommand extends GlobalCommand {
        
        @Option(name = "-gr1", type = OptionType.GROUP)
        protected String gr1;
        
        @Option(name = "-gr2", type = OptionType.GROUP, arity = 2)
        protected String[] gr2;
        
    }
    
    @Command(group = "remote", name = "radd", description = "remote add command")
    public static class RemoteAddCommand extends RemoteCommand {
        
        @Option(name = "-c1")
        protected String c1;
        
        @Option(name = "-c2", arity = 2)
        protected String[] c2;
        
        @Argument(order = 0)
        protected String a1;

        @Argument(order = 1)
        protected String a2;
        
        @Arguments
        protected String[] a3;
        
    }
    
    @Command(group = "remote", name = "remove", description = "remote remove command")
    public static class RemoteRemoveCommand extends RemoteCommand {
        
    }
    
    @Command(name = "add", description = "remote add command")
    public static class GroupAddCommand extends GlobalCommand  {
        
    }
    
    @Command(name = "remove", description = "remote remove command")
    public static class GroupRemoveCommand extends GlobalCommand {
        
    }
}
