package com.sangupta.outline;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for building command outlines, or command groups.
 * 
 * @author sangupta
 *
 */
public abstract class OutlineBase {
    
    final String name;
    
    String description;
    
    Class<?> defaultCommand;
    
    final Set<Class<?>> commands = new HashSet<>();
    
    /**
     * prevent default construction
     * 
     */
    OutlineBase(String name) {
        this.name = name;
    }
    
    public OutlineBase withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public OutlineBase withDefaultCommand(Class<?> defaultCommand) {
        this.defaultCommand = defaultCommand;
        return this;
    }
    
    public OutlineBase withCommands(Class<?>... commands) {
        if(commands == null || commands.length == 0) {
            return this;
        }
        
        for(Class<?> command : commands) {
            this.commands.add(command);
        }
        
        return this;
    }

    public boolean hasCommand(Class<?> commandClass) {
        return this.commands.contains(commandClass);
    }
    
}
