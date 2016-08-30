package com.sangupta.outline;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.outline.annotations.Argument;
import com.sangupta.outline.annotations.Arguments;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.annotations.OptionType;
import com.sangupta.outline.exceptions.InvalidOutlineConfigurationException;
import com.sangupta.outline.util.OutlineUtil;

/**
 * Functions that read all the metadata from classes via annotations and otherwise
 * and make sure that we have all the required information to start processing.
 * 
 * @author sangupta
 *
 */
public class OutlineMetadataReader {
	
	/**
     * My private logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OutlineMetadataReader.class);

    public static final Comparator<Object> ARGUMENTS_SORTER = new Comparator<Object>() {
		
		@Override
		public int compare(Object o1, Object o2) {
			if(!isValidType(o1) || !isValidType(o2)) {
				throw new IllegalArgumentException("The collection should only have Argument/Arguments");
			}
			
			if(o1 instanceof Arguments) {
				return 1;
			}
			
			if(o2 instanceof Arguments) {
				return -1;
			}
			
			Argument a1 = (Argument) o1;
			Argument a2 = (Argument) o2;
			
			return Integer.compare(a1.order(), a2.order()); 
		}
		
		private boolean isValidType(Object obj) {
			if(obj instanceof Argument) {
				return true;
			}
			
			if(obj instanceof Arguments) {
				return true;
			}
			
			return false;
		}
		
	};

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
            readCommandData(commandClass, outline, metadata);
        }
        
        // sort arguments
        sortCommandArguments(metadata);
        
        return metadata;
    }



	private static void sortCommandArguments(OutlineMetadata metadata) {
		if(!metadata.commandArguments.isEmpty()) {
            Set<String> keys = metadata.commandArguments.keySet();
            for(String key : keys) {
            	List<Object> arguments = metadata.commandArguments.getValues(key);
            	Collections.sort(arguments, ARGUMENTS_SORTER);
            }
        	
        }
	}
    


	private static void readCommandData(Class<?> commandClass, Outline outline, OutlineMetadata metadata) {
		Command command = commandClass.getAnnotation(Command.class);
        if(command == null) {
            LOGGER.warn("Command class {} does not have a @Command annotation, skipping loading command.", commandClass.getName());
            return;
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
        
        readFieldData(commandClass, metadata, commandName, group);
	}
	
	private static void readFieldData(Class<?> commandClass, OutlineMetadata metadata, final String commandName, String group) {
		// read options from within the commandClass
        List<Field> fields = OutlineUtil.getAllFields(commandClass);
        if(fields.isEmpty()) {
            // there are no fields in command
            // let's move on
            return;
        }
        
        // we do have fields available - let's check each one
        for(Field field : fields) {
            readFieldData(metadata, commandName, group, field);
        }
	}

	private static OptionType readFieldData(OutlineMetadata metadata, final String commandName, String group, Field field) {
		// check if command accepts arguments or not
		Argument argument = field.getAnnotation(Argument.class);
		if(argument != null) {
			metadata.commandArguments.put(commandName, argument);
		}
		
		Arguments arguments = field.getAnnotation(Arguments.class);
		if(arguments != null) {
			metadata.commandArguments.put(commandName, arguments);
		}
		
		// check if field is annotated with @Option annotation
		Option option = field.getAnnotation(Option.class);
		if(option == null) {
		    return null;
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
		    	if(metadata.singleCommandMode) {
		    		throw new InvalidOutlineConfigurationException(commandName, field.getName(), "Group option specified in single-command mode");
		    	}
		    	
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
		
		return type;
	}
}
