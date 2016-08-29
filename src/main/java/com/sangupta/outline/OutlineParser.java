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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ReflectionUtils;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.annotations.OptionType;
import com.sangupta.outline.help.OutlineHelpCommand;
import com.sangupta.outline.parser.ArgumentParser;
import com.sangupta.outline.parser.ParseResult;
import com.sangupta.outline.util.OutlineUtil;

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
        OutlineMetadata metadata = getMetadata(outline);
        
        if(args.length == 0) {
            if(outline.defaultCommand == null) {
                return null;
            }
            
            return outline.commandFactory.createInstance(outline.defaultCommand);
        }
        
        // start reading the options and making sure that we know where the command break-point is
        ParseResult result = ArgumentParser.parse(args, metadata);
        
        if(result.command == null) {
            throw new RuntimeException("command not specified");
        }
        
        // check if we are requesting help
        boolean isHelpRequested = result.command.equals(outline.helpKeyword);
        if(isHelpRequested) {
        	final OutlineHelpCommand helpCommand = new OutlineHelpCommand(metadata, result);
        	
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
        
        // bind the object to its available properties
        OutlineBinder.bindInstanceToProperties(inferredCommand, instance, result);
        
        // return the instance
        return instance;
    }

    private static void injectHelpOptionsIfAvailable(Object instance, OutlineHelpCommand helpCommand) {
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
			if(!OutlineHelpCommand.class.isAssignableFrom(field.getType())) {
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

	/**
     * Read the {@link Outline} and generate the {@link OutlineMetadata} that contains
     * all information that is required to populate command needed, and all its arguments.
     *  
     * @param outline
     * @return
     */
    static OutlineMetadata getMetadata(Outline outline) {
        OutlineMetadata metadata = new OutlineMetadata(outline);
        
        // the first thing we add is the help keyword
        // the command can be set to null - as we do not need to define it now
        metadata.commandNames.put(outline.helpKeyword, null);
        
        // read all command classes and figure out all commands
        for(Class<?> commandClass : outline.commands) {
            // read command details
            Command command = commandClass.getAnnotation(Command.class);
            if(command == null) {
                LOGGER.warn("Command class {} does not have a @Command annotation, skipping loading command.", commandClass.getName());
                continue;
            }
            
            final String commandName = command.name();
            
            if(AssertUtils.isEmpty(commandName)) {
                LOGGER.warn("Command does not have a valid name");
            }
            
            // the command is valid
            metadata.commandNames.put(commandName, command);
            metadata.commandClasses.put(commandName, commandClass);
            
            // the command group, if any
            String group = outline.getCommandGroup(commandClass);
            if(group != null) {
                if(AssertUtils.isNotEmpty(command.group())) {
                    LOGGER.warn("Command specifies a group and has also been added via API, the API one will supersede");
                }
            } else {
                group = command.group();
            }
            
            if(group != null && !group.trim().isEmpty()) {
                // command has an associated group
                metadata.commandGroups.put(group, command);
            }
            
            // read options from within the commandClass
            List<Field> fields = OutlineUtil.getAllFields(commandClass);
            if(fields.isEmpty()) {
                // there are no fields in command
                // let's move on
                continue;
            }
            
            // we do have fields available - let's check each one
            for(Field field : fields) {
                // check if field is annotated with @Option annotation
                Option option = field.getAnnotation(Option.class);
                if(option == null) {
                    // field is not annotated
                    continue;
                }
                
                // the field is an argument - read it
                OptionType type = option.type();
                Map<String, Option> map;
                switch(type) {
                    case COMMAND:
                        map = metadata.commandOptions.get(commandName);
                        if(map == null) {
                            map = new HashMap<>();
                            metadata.commandOptions.put(commandName, map);
                        }
                        
                        for(String name : option.name()) {
                            map.put(name, option);
                        }
                        break;
                    
                    case GLOBAL:
                        for(String name : option.name()) {
                            metadata.globalOptions.put(name, option);
                        }
                        break;
                    
                    case GROUP:
                        if(group == null) {
                            throw new RuntimeException("Group option from command is not a part of any group");
                        }

                        map = metadata.groupOptions.get(group);
                        if(map == null) {
                            map = new HashMap<>();
                            metadata.groupOptions.put(group, map);
                        }
                        
                        for(String name : option.name()) {
                            map.put(name, option);
                        }
                        break;
                    
                    default:
                        throw new IllegalStateException("Unkown option type detected: " + type.toString());
                }
            }
        }
        
        return metadata;
    }

}
