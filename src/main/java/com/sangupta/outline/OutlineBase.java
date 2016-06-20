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
