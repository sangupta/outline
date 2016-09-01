package com.sangupta.outline.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.outline.Outline;
import com.sangupta.outline.OutlineMetadata;
import com.sangupta.outline.annotations.Argument;
import com.sangupta.outline.annotations.Arguments;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.parser.ParseResult;
import com.sangupta.outline.util.OutlineUtil;

public class OutlineHelpBuilder {
	
	/**
	 * The metadata associated with this {@link Outline} generation
	 * 
	 */
    protected final OutlineMetadata meta;
    
    /**
     * The result of parsing the arguments
     */
    protected final ParseResult result;

    /**
     * Construct an instance of {@link OutlineHelpBuilder}.
     * 
     * @param meta
     * @param result
     */
    public OutlineHelpBuilder(OutlineMetadata meta, ParseResult result) {
    	this.meta = meta;
    	this.result = result;
	}

	/**
     * Construct the help text for whatever has been requested.
     * 
     * @return
     */
    public String getHelpText() {
        List<String> helpLines = getHelpLines();
        return HelpFormatter.format(helpLines);
    }
    
    /**
     * Create the help lines for the given command.
     * 
     * @return
     */
    public List<String> getHelpLines() {
    	final String command;
    	final String group;
    	
        if(this.meta.singleCommandMode) {
        	command = this.meta.commandClasses.keySet().iterator().next();
            group = null;
        } else {
        	command = this.result.command;
        	group = this.result.group;
        }
        
        List<String> lines = new ArrayList<>();
        
        lines.add(this.meta.name + ": " + this.meta.description);
        lines.add(null);
        
        lines.add("Usage:");
        lines.add(getUsageLine(command, group));
        lines.add(null);

        lines.addAll(getOptionsSection(command, group));
        lines.add(null);

        lines.addAll(getCommandsSection(command, group));
        lines.add(null);
        
        lines.addAll(getArgumentsSection(command, group));
        
        return lines;
    }

    /**
     * Show all commands that are applicable.
     * 
     * @param command
     * @param group
     * @return
     */
    private List<String> getCommandsSection(String command, String group) {
    	List<String> lines = new ArrayList<>();
    	
    	if(this.meta.singleCommandMode) {
    		// not applicable for single command mode
    		return lines;
    	}
    	
    	if(AssertUtils.isNotEmpty(command)) {
    		return lines;
    	}

    	lines.add("Available commands:");
    	lines.add(null);
    	
    	Collection<Command> commands;
    	if(AssertUtils.isEmpty(group)) {
    		commands = this.meta.commandNames.values();
    	} else {
    		commands = this.meta.commandGroups.getValues(group);
    	}
    	
        Set<Command> commandSet = new HashSet<>(commands);
        for(Command item : commandSet) {
        	if(item == null) {
    			// this happens because of the help keyword
        		continue;
        	}
        	
        	if(item.hidden()) {
        		// command is hidden
        		continue;
        	}
        	
            lines.add("\t" + item.name());
            lines.add("\t\t" + item.description());
            lines.add(null);
        }

		return lines;
	}

	/**
     * Create the help lines for applicable arguments to the command.
     * 
     * @return
     */
    private List<String> getArgumentsSection(final String command, final String group) {
    	List<String> lines = new ArrayList<>();

    	lines.add("Available arguments:");
    	lines.add(null);
		lines.addAll(buildArgumentsForCommand(command, group));

		return lines;
    }

    /**
     * Create the list of all arguments that are applicable to this particular command.
     * 
     * @param class1
     * @return
     */
    private List<String> buildArgumentsForCommand(final String command, final String group) {
		List<String> lines = new ArrayList<>();
		
    	if(AssertUtils.isEmpty(command)) {
    		return lines;
    	}
    	
		List<Object> arguments = this.meta.commandArguments.getValues(command);
		if(AssertUtils.isEmpty(arguments)) {
			return lines;
		}
		
		int count = 1;
		for(Object obj : arguments) {
			// iterate over them
			if(obj instanceof Argument) {
				Argument arg = (Argument) obj;
				
				lines.add("\t<" + nonEmpty(arg.title(), "arg" + count++) + ">");
				lines.add("\t\t" + arg.description());
				lines.add(null);
			}
			
			if(obj instanceof Arguments) {
				Arguments args = (Arguments) obj;

				lines.add("\t<" + nonEmpty(args.title(), "arguments") + ">");
				lines.add("\t\t" + args.description());
				lines.add(null);
			}
		}
		
		return lines;
	}
    
    /**
     * Return the first non-empty value.
     * 
     * @param value1
     * @param value2
     * @return
     */
    private static String nonEmpty(String value1, String value2) {
    	if(AssertUtils.isNotEmpty(value1)) {
    		return value1;
    	}
    	
    	return value2;
    }

	/**
     * Create the help lines for applicable options.
     * 
     * @return
     */
    private List<String> getOptionsSection(final String command, final String group) {
        List<String> lines = new ArrayList<>();
        
        if(this.meta.globalOptions.isEmpty() && this.meta.commandOptions.isEmpty()) {
        	return lines;
        }
        
        lines.add("Available options:");
        lines.add(null);
        
        // add all global options
        lines.addAll(buildOptionsSectionForData(this.meta.globalOptions));
        
        // build for group options - only if needed
        if(!this.meta.commandGroups.isEmpty()) {
            
        }
        
        // add all command options
        if(this.result.command != null) {
        	if(this.meta.singleCommandMode) {
        		lines.addAll(buildOptionsSectionForData(this.meta.commandOptions.values().iterator().next()));
        	} else {
        		lines.addAll(buildOptionsSectionForData(this.meta.commandOptions.get(this.result.command)));
        	}
        }
        
        return lines;
    }

    /**
     * Build the options list.
     * 
     * @param options
     * @return
     */
    private List<String> buildOptionsSectionForData(Map<String, Option> optionMap) {
        List<String> lines = new ArrayList<>();
        
        if(AssertUtils.isEmpty(optionMap)) {
        	return lines;
        }

        Collection<Option> optionsCollection = optionMap.values();
        
        Set<Option> options = new HashSet<>(optionsCollection);
        for(Option option : options) {
        	if(option.hidden()) {
        		// skip hidden options
        		continue;
        	}
        	
            String[] names = option.name();
            lines.add("\t" + OutlineUtil.join(names, ", "));
            lines.add("\t\t" + getOptionHelp(option));
            lines.add(null);
        }
        
        return lines;
    }
    
    private String getOptionHelp(Option option) {
    	StringBuilder builder = new StringBuilder(1024);
    	
    	if(option.required()) {
    		builder.append("Required. ");
    	}
    	
    	builder.append(option.description());
    	if(!option.description().endsWith(".")) {
    		builder.append(". ");
    	}
    	
    	if(option.arity() > 0) {
	        Class<?> optionClass = this.meta.optionFieldType.get(option);
	        builder.append("Expects argument of type: ");
	        builder.append(optionClass.getSimpleName());
	        builder.append(". ");
    	}
    	
    	if(AssertUtils.isNotEmpty(option.allowedValues())) {
    		builder.append("Allowed values: ");
    		builder.append(OutlineUtil.join(option.allowedValues(), ", "));
    	}
    	
    	return builder.toString();
    }

	/**
     * Construct the usage line.
     * 
     * @return
     */
    private String getUsageLine(final String command, final String group) {
    	StringBuilder builder = new StringBuilder(1024);
    	
    	builder.append('\t');
    	builder.append(this.meta.name);
    	builder.append(' ');
    	
    	// all global options must be shown here
		// global
		if(AssertUtils.isNotEmpty(this.meta.globalOptions)) {
			Set<Option> options = new HashSet<>(this.meta.globalOptions.values());
    		buildOptionsSectionInUsage(builder, options);
    	}
		
    	// then we show the command
    	// this is only applicable for multi-command mode
    	if(!this.meta.singleCommandMode) {
    		if(AssertUtils.isNotEmpty(command)) {
    			builder.append(' ');
    			builder.append(command);
    		} else {
    			builder.append(" <command>");
    		}
    	}
    	
		// group
		if(AssertUtils.isNotEmpty(group)) {
			Set<Option> options = new HashSet<>(this.meta.groupOptions.get(group).values());
			buildOptionsSectionInUsage(builder, options);
		}
		
		// command options
		if(AssertUtils.isNotEmpty(command)) {
			Set<Option> options = new HashSet<>(this.meta.commandOptions.get(command).values());
			buildOptionsSectionInUsage(builder, options);
		}
		
    	// and lastly we need to display the arguments that can be passed
    	// this needs to be displayed, only if there are arguments that can be applied
    	// to a command
    	getArgumentSectionInUsage(command, builder);
		
    	return builder.toString();
    }

    /**
     * Build the arguments section in the usage line.
     * 
     * @param command
     * @param builder
     */
	private void getArgumentSectionInUsage(final String command, StringBuilder builder) {
		if(AssertUtils.isEmpty(command)) {
			return;
		}
		
		List<Object> arguments = this.meta.commandArguments.getValues(command);
		
		if(AssertUtils.isEmpty(arguments)) {
			if(!this.meta.singleCommandMode) {
				builder.append(" <args>");
			}

			return;
		}
		
		int count = 1;
		for(Object obj : arguments) {
			// iterate over them
			if(obj instanceof Argument) {
				Argument arg = (Argument) obj;

				builder.append(' ');
				builder.append('<');
				builder.append(nonEmpty(arg.title(), "arg" + count++));
				builder.append('>');
			}
			
			if(obj instanceof Arguments) {
				Arguments args = (Arguments) obj;

				builder.append(' ');
				builder.append('<');
				builder.append(nonEmpty(args.title(), "arguments"));
				builder.append('>');
			}
		}
	}

	private void buildOptionsSectionInUsage(StringBuilder builder, Set<Option> options) {
		boolean first = true;
		
		// show one for each of the param
		for(Option option : options) {
			if(option.hidden()) {
				// skip hidden option
				continue;
			}
			
			String[] names = option.name();
			builder.append(" [");
			
			first = true;
			if(names.length > 1) {
				builder.append('(');
			}
			
			for(String name : names) {
				if(!first) {
					builder.append(" | ");
				}
				
				first = false;
				builder.append(name);
			}
			
			if(names.length > 1) {
				builder.append(')');
			}

			for(int index = 0; index < option.arity(); index++) {
				if(option.arity() == 1) {
					builder.append(" <option-arg>");
				} else {
					builder.append(" <option-arg" + (index + 1) + ">");
				}
			}
			
			builder.append(']');
		}
	}

}
