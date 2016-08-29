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
 
package com.sangupta.outline;

import java.util.HashSet;
import java.util.Set;

import com.sangupta.outline.annotations.Command;

/**
 * Base class for building command outlines, or command groups.
 * 
 * @author sangupta
 *
 */
public abstract class OutlineBase {
    
    final String name;
    
    final boolean singleCommandMode;
    
    String description;
    
    Class<?> defaultCommand;
    
	boolean helpOnIncorrectArguments = true;
    
    final Set<Class<?>> commands = new HashSet<>();
    
    /**
     * prevent default construction
     * 
     */
    OutlineBase(String name) {
        this.name = name;
        this.singleCommandMode = false;
    }
    
    OutlineBase(Class<?> singleCommandClass) {
        Command command = singleCommandClass.getAnnotation(Command.class);
        if(command == null) {
            throw new IllegalArgumentException("Command must be annotated with @Command annotation");
        }
        
        this.name = command.name();
        this.description = command.description();
        
        this.defaultCommand = singleCommandClass;
        this.commands.add(singleCommandClass);
        
        this.singleCommandMode = true;
    }
    
    public OutlineBase withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public OutlineBase withDefaultCommand(Class<?> defaultCommand) {
        if(this.singleCommandMode) {
            throw new IllegalStateException("Cannot set default command in single-command mode.");
        }
        
        this.defaultCommand = defaultCommand;
        this.commands.add(defaultCommand);
        return this;
    }
    
    public OutlineBase withCommands(Class<?>... commands) {
        if(this.singleCommandMode) {
            throw new IllegalStateException("Cannot add commands in single-command mode.");
        }
        
        if(commands == null || commands.length == 0) {
            return this;
        }
        
        for(Class<?> command : commands) {
            this.commands.add(command);
        }
        
        return this;
    }

    /**
     * Check if we have a command for the given {@link Class}.
     * 
     * @param commandClass
     * @return
     */
    public boolean hasCommand(Class<?> commandClass) {
        return this.commands.contains(commandClass);
    }
    
    /**
     * Whether to return help on incorrect arguments.
     * 
     * @return
     */
    public boolean isHelpOnIncorrectArguments() {
		return this.helpOnIncorrectArguments;
	}

}
