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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.outline.cmdfactory.OutlineCommandFactory;
import com.sangupta.outline.cmdfactory.OutlineDefaultCommandFactory;

/**
 * Entry class for building the outlines of command line tools.
 * 
 * @author sangupta
 *
 */
public class Outline extends OutlineBase {
    
	/**
	 * The help keyword to use - the default value is <code>help</code>
	 */
    String helpKeyword = "help";
    
    /**
     * The command factory to use to create instances of commands. The default
     * value is to use {@link OutlineDefaultCommandFactory} implementation.
     * 
     */
    OutlineCommandFactory commandFactory = new OutlineDefaultCommandFactory();
    
    /**
     * Holds all groups and their definitions
     */
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
     * Register global converters that help us throughout. 
     * 
     */
    static {
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
        super.withDefaultCommand(defaultCommand);
        return this;
    }
    
    @Override
    public Outline withCommand(Class<?> command) {
    	super.withCommand(command);
    	
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

    /**
     * Set the help keyword that shoudl be used.
     * 
     * @param word
     * @return
     */
    public Outline withHelpKeyword(String word) {
        this.helpKeyword = word;
        return this;
    }
    
    /**
     * Use the given command factory to create instances of command rather than the
     * default one.
     * 
     * @param commandFactory
     * @return
     */
    public Outline withCommandFactory(OutlineCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        return this;
    }
 
    /**
	 * Create a new group name and return the group definition so that more
	 * commands can be added to the group. If the group name has been used before
	 * the same previous group definition is returned.
	 * 
	 * @param name
	 *            the group name to use
	 * 
	 * @return the group definition
	 * 
	 * @throws IllegalStateException
	 *             if we are in a single command mode
	 * 
	 * @throws IllegalArgumentException
	 *             if the group name is <code>empty</code> or <code>null</code>
	 */
    public OutlineBase withGroup(String name) {
        if(this.singleCommandMode) {
            throw new IllegalStateException("Cannot create groups in single-command mode.");
        }
        
        if(AssertUtils.isEmpty(name)) {
        	throw new IllegalArgumentException("Group name cannot be null/empty");
        }
        
        // do we already have such a group
        if(this.groups.containsKey(name)) {
        	return this.groups.get(name);
        }
        
        // create a new group
        OutlineBase abstractOutline = new OutlineBase(name) { };
        this.groups.put(name, abstractOutline);
        return abstractOutline;
    }

    /**
	 * Get the name of the group to which the given command class belongs. Will
	 * return <code>null</code> if the command does not belong to any group.
	 * 
	 * @param commandClass
	 *            the command class to check
	 * 
	 * @return the group name if found, <code>null</code> otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             if the command class provided is <code>null</code>
	 */
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

    /**
	 * Set whether help should be displayed by default when incorrect arguments
	 * are presented during application invocation.
	 * 
	 * @param helpOnIncorrectArguments
	 *            <code>true</code> if help should be shown, <code>false</code>
	 *            otherwise
	 * 
	 * @return the same {@link Outline} instance
	 */
	public Outline withHelpOnIncorrectArguments(boolean helpOnIncorrectArguments) {
		this.helpOnIncorrectArguments = helpOnIncorrectArguments;
		return this;
	}

	/**
	 * Return whether we are in a single-command mode or not.
	 * 
	 * @return <code>true</code> if {@link Outline} is operating in a
	 *         single-command mode, <code>false</code> otherwise
	 */
	public boolean isSingleCommandMode() {
		return this.singleCommandMode;
	}
	
}
