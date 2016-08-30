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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ReflectionUtils;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.help.OutlineHelp;
import com.sangupta.outline.parser.ArgumentParser;
import com.sangupta.outline.parser.ParseResult;

/**
 * Parse the {@link Outline} instance along with the provided {@link String} array
 * arguments and return the baked {@link Object} instance that represents the command
 * that needs to be executed.
 * 
 * @author sangupta
 *
 */
public class OutlineParser {
    
    /**
     * My private logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OutlineParser.class);
    
    /**
     * Parse the command line arguments against the given {@link Outline} object
     * and return the {@link Command} instance that the user had requested execution for.
     * 
     * @param outline
     * @param args
     * @return
     */
    public static Object parse(Outline outline, String[] args) {
        // read outline to fetch a list of all options
        OutlineMetadata metadata = OutlineMetadataReader.getMetadata(outline);
        
        if(args.length == 0) {
            if(outline.defaultCommand == null) {
            	LOGGER.debug("No arguments specified, no default command, return null");
                return null;
            }
            
            LOGGER.debug("No arguments specified, return default command: {}", outline.defaultCommand);
            return outline.commandFactory.createInstance(outline.defaultCommand);
        }
        
        // start reading the options and making sure that we know where the command break-point is
        ParseResult result = ArgumentParser.parse(args, metadata);
        
        if(!result.helpRequested && result.command == null) {
            throw new RuntimeException("command not specified");
        }
        
        // create the help command
    	final OutlineHelp helpCommand = new OutlineHelp(metadata, result);
    	
    	// if help has been requested, return the help command or the instance with help injected
        if(result.helpRequested) {
        	if(metadata.singleCommandMode) {
        		// this is single command mode
        		// we need to throw back the command instance back that has an injection of the OutlineHelpCommand property
        		Class<?> instanceClass = metadata.commandClasses.values().iterator().next();
        		Object instance = outline.commandFactory.createInstance(instanceClass);
        		
        		// inject the help options in this object
        		injectHelpOptionsIfAvailable(instance, helpCommand);
        		
        		// return the prepared instance
        		return instance;
        	}
        	
            return helpCommand; 
        }
        
        // get the command class we need to work with
        Class<?> inferredCommand = metadata.commandClasses.get(result.command);
        Object instance = outline.commandFactory.createInstance(inferredCommand);
        
        // inject the help options in this object
		injectHelpOptionsIfAvailable(instance, helpCommand);
        
        // bind the object to its available properties
        OutlineBinder.bindInstanceToProperties(inferredCommand, instance, result);
        
        // return the instance
        return instance;
    }

    /**
     * Inject the field of type {@link OutlineHelp} in the given object instance if a field
     * annotated with <code>@Inject</code> of the same type is present. Does nothing if either
     * of instance or helpCommand value is <code>null</code>.
     * 
     * @param instance the instance in which to inject
     * 
     * @param helpCommand the value to inject
     */
    private static void injectHelpOptionsIfAvailable(Object instance, OutlineHelp helpCommand) {
		Field[] fields = instance.getClass().getFields();
		if(AssertUtils.isEmpty(fields)) {
			return;
		}
		
		for(Field field : fields) {
			Inject inject = field.getAnnotation(Inject.class);
			if(inject == null) {
				continue;
			}
			
			// injection is enabled
			// let's check field type
			if(!OutlineHelp.class.isAssignableFrom(field.getType())) {
				continue;
			}
			
			try {
				ReflectionUtils.bindValue(field, instance, helpCommand);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
