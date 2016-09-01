package com.sangupta.outline.help;

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
        IndentedStringWriter writer = getHelpLines();
        return writer.toString();
    }
    
    /**
     * Create the help lines for the given command.
     * 
     * @return
     */
    public IndentedStringWriter getHelpLines() {
    	final IndentedStringWriter writer = new IndentedStringWriter();
    	
    	final String command = this.result.command;
    	final String group = this.result.group;
        
        writer.writeLine(this.meta.name + ": " + this.meta.description);
        writer.newLine();
        
        writer.writeLine("Usage:");
        getUsageLine(writer, command, group);
        writer.newLine();

        getOptionsSection(writer, command, group);
        writer.newLine();

        getCommandsSection(writer, command, group);
        writer.newLine();
        
        getArgumentsSection(writer, command, group);
        
        return writer;
    }

    /**
     * Show all commands that are applicable.
     * 
     * @param command
     * @param group
     * @return
     */
    private void getCommandsSection(final IndentedStringWriter writer, final String command, final String group) {
    	if(this.meta.singleCommandMode) {
    		// not applicable for single command mode
    		return;
    	}
    	
    	if(AssertUtils.isNotEmpty(command)) {
    		return;
    	}

    	writer.writeLine("Available commands:");
    	
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
        	
        	writer.setIndentLevel(1);
            writer.write(item.name());
            writer.setIndentLevel(1);
            writer.writeLine(item.description());
        }
	}

	/**
     * Create the help lines for applicable arguments to the command.
     * 
     * @return
     */
    private void getArgumentsSection(final IndentedStringWriter writer, final String command, final String group) {
    	writer.writeLine("Available arguments:");
		buildArgumentsForCommand(writer, command, group);
    }

    /**
     * Create the list of all arguments that are applicable to this particular command.
     * 
     * @param class1
     * @return
     */
    private void buildArgumentsForCommand(final IndentedStringWriter writer, final String command, final String group) {
    	if(AssertUtils.isEmpty(command)) {
    		return;
    	}
    	
		List<Object> arguments = this.meta.commandArguments.getValues(command);
		if(AssertUtils.isEmpty(arguments)) {
			return;
		}
		
		int count = 1;
		for(Object obj : arguments) {
			// iterate over them
			if(obj instanceof Argument) {
				Argument arg = (Argument) obj;
				
				writer.setIndentLevel(1);
				writer.writeLine("<" + nonEmpty(arg.title(), "arg" + count++) + ">");
				writer.setIndentLevel(2);
				writer.writeLine(arg.description());
			}
			
			if(obj instanceof Arguments) {
				Arguments args = (Arguments) obj;

				writer.setIndentLevel(1);
				writer.writeLine("<" + nonEmpty(args.title(), "arguments") + ">");
				writer.setIndentLevel(2);
				writer.writeLine(args.description());
			}
		}
		
		writer.setIndentLevel(0);
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
    private void getOptionsSection(final IndentedStringWriter writer, final String command, final String group) {
        if(this.meta.globalOptions.isEmpty() && this.meta.commandOptions.isEmpty()) {
        	return;
        }
        
        writer.writeLine("Available options:");
        
        // add all global options
        buildOptionsSectionForData(writer, this.meta.globalOptions);
        
        // build for group options - only if needed
        if(!this.meta.commandGroups.isEmpty()) {
            
        }
        
        // add all command options
        if(AssertUtils.isNotEmpty(this.result.command)) {
    		buildOptionsSectionForData(writer, this.meta.commandOptions.values().iterator().next());
        }
    }

    /**
     * Build the options list.
     * 
     * @param options
     * @return
     */
    private void buildOptionsSectionForData(final IndentedStringWriter writer, final Map<String, Option> optionMap) {
        if(AssertUtils.isEmpty(optionMap)) {
        	return;
        }

        Collection<Option> optionsCollection = optionMap.values();
        
        Set<Option> options = new HashSet<>(optionsCollection);
        for(Option option : options) {
        	if(option.hidden()) {
        		// skip hidden options
        		continue;
        	}
        	
            String[] names = option.name();
            writer.setIndentLevel(1);
            writer.writeLine(OutlineUtil.join(names, ", "));
            writer.setIndentLevel(2);
            getOptionHelp(writer, option);
            writer.setIndentLevel(0);
        }
    }
    
    private void getOptionHelp(IndentedStringWriter writer, Option option) {
    	writer.write(option.description());
    	if(!option.description().endsWith(".")) {
    		writer.write(".");
    	}
    	
    	writer.newLine();
    	writer.newLine();
    	
    	if(option.required()) {
    		writer.write("Required. ");
    	}
    	
    	if(option.arity() > 0) {
	        Class<?> optionClass = this.meta.optionFieldType.get(option);
	        writer.write("Expects argument of type: ");
	        writer.write(optionClass.getSimpleName());
	        writer.write(". ");
    	}
    	
    	if(AssertUtils.isNotEmpty(option.allowedValues())) {
    		writer.write("Allowed values: ");
    		writer.write(OutlineUtil.join(option.allowedValues(), ", "));
    	}
    }

	/**
     * Construct the usage line.
     * 
     * @return
     */
    private void getUsageLine(final IndentedStringWriter writer, final String command, final String group) {
    	writer.setIndentLevel(1);
    	
    	writer.write("$ ");
    	writer.write(this.meta.name);
    	writer.write(' ');
    	
    	// all global options must be shown here
		// global
		if(AssertUtils.isNotEmpty(this.meta.globalOptions)) {
			Set<Option> options = new HashSet<>(this.meta.globalOptions.values());
    		buildOptionsSectionInUsage(writer, options);
    	}
		
    	// then we show the command
    	// this is only applicable for multi-command mode
    	if(!this.meta.singleCommandMode) {
    		if(AssertUtils.isNotEmpty(command)) {
    			writer.write(' ');
    			writer.write(command);
    		} else {
    			writer.write(" <command>");
    		}
    	}
    	
		// group
		if(AssertUtils.isNotEmpty(group)) {
			Set<Option> options = new HashSet<>(this.meta.groupOptions.get(group).values());
			buildOptionsSectionInUsage(writer, options);
		}
		
		// command options
		if(AssertUtils.isNotEmpty(command)) {
			Set<Option> options = new HashSet<>(this.meta.commandOptions.get(command).values());
			buildOptionsSectionInUsage(writer, options);
		}
		
    	// and lastly we need to display the arguments that can be passed
    	// this needs to be displayed, only if there are arguments that can be applied
    	// to a command
    	getArgumentSectionInUsage(command, writer);
    	
    	// reset indentation
    	writer.setIndentLevel(0);
    }

    /**
     * Build the arguments section in the usage line.
     * 
     * @param command
     * @param builder
     */
	private void getArgumentSectionInUsage(final String command, final IndentedStringWriter writer) {
		if(AssertUtils.isEmpty(command)) {
			return;
		}
		
		List<Object> arguments = this.meta.commandArguments.getValues(command);
		
		if(AssertUtils.isEmpty(arguments)) {
			if(!this.meta.singleCommandMode && AssertUtils.isEmpty(command)) {
				writer.write(" <args>");
			}

			return;
		}
		
		int count = 1;
		for(Object obj : arguments) {
			// iterate over them
			if(obj instanceof Argument) {
				Argument arg = (Argument) obj;

				writer.write(' ');
				writer.write('<');
				writer.write(nonEmpty(arg.title(), "arg" + count++));
				writer.write('>');
			}
			
			if(obj instanceof Arguments) {
				Arguments args = (Arguments) obj;

				writer.write(' ');
				writer.write('<');
				writer.write(nonEmpty(args.title(), "arguments"));
				writer.write('>');
			}
		}
	}

	private void buildOptionsSectionInUsage(IndentedStringWriter writer, Set<Option> options) {
		boolean first = true;
		
		// show one for each of the param
		for(Option option : options) {
			if(option.hidden()) {
				// skip hidden option
				continue;
			}
			
			String[] names = option.name();
			writer.write(" [");
			
			first = true;
			if(names.length > 1) {
				writer.write('(');
			}
			
			for(String name : names) {
				if(!first) {
					writer.write(" | ");
				}
				
				first = false;
				writer.write(name);
			}
			
			if(names.length > 1) {
				writer.write(')');
			}

			for(int index = 0; index < option.arity(); index++) {
				if(option.arity() == 1) {
					writer.write(" <option-arg>");
				} else {
					writer.write(" <option-arg" + (index + 1) + ">");
				}
			}
			
			writer.write(']');
		}
	}

}
