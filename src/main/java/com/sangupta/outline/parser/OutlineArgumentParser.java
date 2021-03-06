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

import java.util.Map;

import com.sangupta.jerry.ds.SimpleMultiMap;
import com.sangupta.jerry.ds.iterator.StringArrayIterator;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.outline.OutlineMetadata;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.exceptions.OutlineInvalidArgumentException;

public class OutlineArgumentParser {

    public static OutlineParseResult parse(String[] args, OutlineMetadata metadata) {
        StringArrayIterator iterator = new StringArrayIterator(args);
        OutlineParseResult result = new OutlineParseResult();
        
        // first let's read the global options
        readGlobalOptions(result, iterator, metadata);

        // read group and group options if applicable
        readGroupAndGroupOptions(result, iterator, metadata);
        
        // read command name and command options if applicable
        readCommandNameAndOptions(result, iterator, metadata);
        
        // whatever is remaining is now the command arguments
        readCommandArguments(result, iterator);
        
        return result;
    }

    /**
     * Read all remaining values as command arguments
     * 
     * @param result
     * @param iterator
     */
    private static void readCommandArguments(OutlineParseResult result, StringArrayIterator iterator) {
        while(iterator.hasNext()) {
            result.arguments.add(iterator.next());
        }
    }

    /**
     * Read the command name and command options
     * 
     * @param result
     * @param iterator
     * @param metadata
     */
    private static void readCommandNameAndOptions(OutlineParseResult result, StringArrayIterator iterator, OutlineMetadata metadata) {
        if(!iterator.hasNext()) {
            return;
        }
        
        String name = iterator.peek();
        if(!metadata.commandNames.containsKey(name)) {
        	// check if this is help required
        	if(name.equals(metadata.helpKeyword)) {
            	name = iterator.next();
            	
        		result.helpRequested = true;
        		
        		// check if we have more values
        		if(!iterator.hasNext()) {
        			return;
        		}
        		String commandOrGroup = iterator.next();
        		if(metadata.commandGroups.containsKey(commandOrGroup)) {
        			// this is group name
        			result.group = commandOrGroup;
        			
        			// let's check if we also have a command name
        			if(!iterator.hasNext()) {
        				return;
        			}
        			
        			result.command = iterator.next();
        			return;
        		}
        		
        		result.command = commandOrGroup;
        		return;
        	}
        	
        	if(metadata.helpOnIncorrectArguments) {
        		result.helpRequested = true;
        		return;
        	}
        	
            throw new OutlineInvalidArgumentException(iterator.peek(), iterator.getPosition());
        }
        
        // the command name
        result.command = iterator.next();
        
        // now that we have a command - let's see if there are any command options that need to be read
        Map<String, Option> options = metadata.commandOptions.get(name);
        if(AssertUtils.isEmpty(options)) {
            return;
        }
        
        while(iterator.hasNext()) {
            String token = iterator.peek();
            
            if(token.equals("--")) {
            	// we have hit the separator
            	break;
            }
            
            if(!options.containsKey(token)) {
                break;
            }
            
            // we found the option
            token = iterator.next();
            parseOption(iterator, options.get(token), result.commandOptions);
        }
    }

    /**
     * Read the command name and optionally the sub-command name if applicable.
     * 
     * @param result
     * @param iterator
     * @param metadata
     */
    private static void readGroupAndGroupOptions(OutlineParseResult result, StringArrayIterator iterator, OutlineMetadata metadata) {
        if(!iterator.hasNext()) {
            return;
        }
        
        String name = iterator.peek();
        if(!metadata.commandGroups.containsKey(name)) {
            return;
        }
        
        result.group = iterator.next();
        
        // now that we have a group - let's see if there are any group options that need to be read
        Map<String, Option> options = metadata.groupOptions.get(name);
        if(AssertUtils.isEmpty(options)) {
            return;
        }
        
        while(iterator.hasNext()) {
            String token = iterator.peek();
            
            if(!options.containsKey(token)) {
                return;
            }
            
            // we found the option
            token = iterator.next();
            parseOption(iterator, options.get(token), result.groupOptions);
        }
    }

    /**
     * Read global options.
     * 
     * @param result
     * @param iterator
     * @param metadata
     */
    private static void readGlobalOptions(OutlineParseResult result, StringArrayIterator iterator, OutlineMetadata metadata) {
        if(metadata.globalOptions.isEmpty()) {
            return;
        }
        
        while(iterator.hasNext()) {
            String token = iterator.peek();
            
            if(!metadata.globalOptions.containsKey(token)) {
                return;
            }
            
            // we found the option
            token = iterator.next();
            parseOption(iterator, metadata.globalOptions.get(token), result.globalOptions);
        }
    }

    /**
     * Parse the values based on the given option.
     *  
     * @param iterator
     * @param option
     * @param globalOptions
     */
    private static void parseOption(StringArrayIterator iterator, Option option, SimpleMultiMap<String, String> optionStore) {
        int tokensToRead = option.arity();
        String name = option.name()[0];
        for(int index = 0; index < tokensToRead; index++) {
            optionStore.put(name, iterator.next());
        }
    }

}
