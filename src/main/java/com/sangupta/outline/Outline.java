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
    
    String helpKeyword;
    
    CommandFactory commandFactory = new DefaultCommandFactory();
    
    final Map<String, OutlineBase> groups = new HashMap<>();
    
    public static <T> void registerTypeConverter(Class<T> classOfT, OutlineTypeConverter<T> converter) {
        OutlineBinder.registerTypeConverter(classOfT, converter);
    }
    
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
