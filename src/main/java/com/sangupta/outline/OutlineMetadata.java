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

import com.sangupta.jerry.ds.SimpleMultiMap;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;

public class OutlineMetadata {
    
    public final boolean singleCommandMode;
    
    public final String name;
    
    public final String description;
    
    public final boolean helpOnIncorrectArguments;
    
    public final String helpKeyword;

    public final Map<String, Option> globalOptions = new HashMap<>();
    
    public final Map<String, Class<?>> commandClasses = new HashMap<>();
    
    public final Map<String, Command> commandNames = new HashMap<>();
    
    public final SimpleMultiMap<String, Command> commandGroups = new SimpleMultiMap<>();
    
    public final Map<String, Map<String, Option>> groupOptions = new HashMap<>();
    
    public final Map<String, Map<String, Option>> commandOptions = new HashMap<>();
    
    OutlineMetadata(Outline outline) {
        this.name = outline.name;
        this.description = outline.description;
        this.singleCommandMode = outline.singleCommandMode;
        this.helpOnIncorrectArguments = outline.isHelpOnIncorrectArguments();
        this.helpKeyword = outline.helpKeyword;
    }

    /**
     * Validate the metadata
     * 
     */
    public void validate() {
        // groupname and commandname cannot be same
        // groupname and command name shall have no space
        // groupname and command name shall be only letters and digits
        // single command cannot have two @Arguments annotations
    }
    
}
