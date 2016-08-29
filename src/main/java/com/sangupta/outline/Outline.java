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
    
    String helpKeyword = "help";
    
    CommandFactory commandFactory = new DefaultCommandFactory();
    
    final Map<String, OutlineBase> groups = new HashMap<>();
    
    /**
     * Register a type converter to convert the command line {@link String} based argument to a
     * given type of argument.
     * 
     * @param classOfT
     * @param converter
     */
    public static <T> void registerTypeConverter(Class<T> classOfT, OutlineTypeConverter<T> converter) {
        OutlineBinder.registerTypeConverter(classOfT, converter);
    }
    
    /**
     * Convenience constructor - this is multi-command mode.
     *  
     */
    public Outline(String name) {
        super(name);
    }
    
    public Outline(Class<?> singleCommandClass) {
        super(singleCommandClass);
    }
    
    /**
     * Initialize this {@link Outline}.
     * 
     * @return
     */
    public Outline create() {
        return this;
    }
    
    @SuppressWarnings("unchecked")
	public <T> T parse(String[] args) {
        Object instance = OutlineParser.parse(this, args);
        if(this.singleCommandMode) {
        	return (T) this.defaultCommand.cast(instance);
        }
        
        return (T) instance;
    }
    
    @Override
    public Outline withDescription(String description) {
        this.description = description;
        return this;
    }
    
    @Override
    public Outline withDefaultCommand(Class<?> defaultCommand) {
        if(this.singleCommandMode) {
            throw new IllegalStateException("Cannot set default command in single-command mode.");
        }
        
        this.defaultCommand = defaultCommand;
        return this;
    }
    
    @Override
    public Outline withCommands(Class<?>... commands) {
        if(this.singleCommandMode) {
            throw new IllegalStateException("Cannot add more commands in single-command mode.");
        }
        
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
        if(this.singleCommandMode) {
            throw new IllegalStateException("Cannot create groups in single-command mode.");
        }
        
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

	public Outline withHelpOnIncorrectArguments(boolean helpOnIncorrectArguments) {
		this.helpOnIncorrectArguments = helpOnIncorrectArguments;
		return this;
	}
    
}
