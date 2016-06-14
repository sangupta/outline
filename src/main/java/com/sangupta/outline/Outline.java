package com.sangupta.outline;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sangupta.outline.cmdfactory.CommandFactory;
import com.sangupta.outline.cmdfactory.DefaultCommandFactory;

/**
 * Entry class for building the outlines of command line tools.
 * 
 * @author sangupta
 *
 */
public class Outline extends OutlineBase {
    
    String helpKeyword;
    
    CommandFactory commandFactory = new DefaultCommandFactory();
    
    final Map<String, OutlineBase> groups = new HashMap<>();
    
    /**
     * 
     */
    public Outline(String name) {
        super(name);
    }
    
    /**
     * Initialize this {@link Outline}.
     * 
     * @return
     */
    public Outline create() {
        return this;
    }
    
    public Object parse(String[] args) {
        return OutlineParser.parse(this, args);
    }
    
    @Override
    public Outline withDescription(String description) {
        this.description = description;
        return this;
    }
    
    @Override
    public Outline withDefaultCommand(Class<?> defaultCommand) {
        this.defaultCommand = defaultCommand;
        return this;
    }
    
    @Override
    public Outline withCommands(Class<?>... commands) {
        super.withCommands(commands);
        return this;
    }

    public Outline withHelpKeyword(String word) {
        this.helpKeyword = word;
        return this;
    }
    
    public Outline withCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        return this;
    }
 
    public OutlineBase withGroup(String name) {
        OutlineBase abstractOutline = new OutlineBase(name) { };
        this.groups.put(name, abstractOutline);
        return abstractOutline;
    }

    public String getCommandGroup(Class<?> commandClass) {
        if(this.groups.isEmpty()) {
            return null;
        }
        
        Set<String> keys = this.groups.keySet();
        for(String key : keys) {
            OutlineBase ao = this.groups.get(key);
            if(ao.hasCommand(commandClass)) {
                return key;
            }
        }
        
        return null;
    }
    
}
