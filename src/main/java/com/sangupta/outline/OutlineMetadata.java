package com.sangupta.outline;

import java.util.HashMap;
import java.util.Map;

import com.sangupta.jerry.ds.SimpleMultiMap;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;

public class OutlineMetadata {

    public final Map<String, Option> globalOptions = new HashMap<>();
    
    public final Map<String, Class<?>> commandClasses = new HashMap<>();
    
    public final Map<String, Command> commandNames = new HashMap<>();
    
    public final SimpleMultiMap<String, Command> commandGroups = new SimpleMultiMap<>();
    
    public final Map<String, Map<String, Option>> groupOptions = new HashMap<>();
    
    public final Map<String, Map<String, Option>> commandOptions = new HashMap<>();
    
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
