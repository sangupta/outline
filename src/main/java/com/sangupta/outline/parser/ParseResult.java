package com.sangupta.outline.parser;

import java.util.ArrayList;
import java.util.List;

import com.sangupta.jerry.ds.SimpleMultiMap;

public class ParseResult {
    
    public String group;
    
    public String command;
    
    public final SimpleMultiMap<String, String> globalOptions = new SimpleMultiMap<>();
    
    public final SimpleMultiMap<String, String> groupOptions = new SimpleMultiMap<>();
    
    public final SimpleMultiMap<String, String> commandOptions = new SimpleMultiMap<>();
    
    public final List<String> arguments = new ArrayList<>();

}
