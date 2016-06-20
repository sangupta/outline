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
