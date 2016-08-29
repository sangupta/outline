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
 
package com.sangupta.outline.help;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.outline.Outline;
import com.sangupta.outline.OutlineMetadata;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.parser.ParseResult;
import com.sangupta.outline.util.OutlineUtil;

/**
 * Represents HELP for the Outline based commands.
 * 
 * @author sangupta
 *
 */
public class OutlineHelp {
    
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
     * Indicates whether help been requested
     */
	protected final boolean helpRequested;

	/**
	 * Convenience constructor.
	 * 
	 * @param metadata
	 * @param result
	 * @param helpRequested
	 */
    public OutlineHelp(OutlineMetadata metadata, ParseResult result, boolean helpRequested) {
        this.meta = metadata;
        this.result = result;
        this.helpRequested = helpRequested;
    }

    /**
     * Returns whether help has been requested by the user or not.
     * 
     * @return
     */
    public boolean isHelpRequested() {
		return helpRequested;
	}

    /**
     * Display help if it has been requested. Returns <code>true</code> if help was
     * requested, <code>false</code> otherwise.
     * 
     * @return
     */
    public boolean showHelpIfRequested() {
    	if(this.helpRequested) {
    		this.showHelp();
    		return true;
    	}
    	
    	return false;
    }

    /**
     * Show the help on console: <code>System.out</code>, even if it has not been requested.
     * 
     */
    public void showHelp() {
    	this.showHelp(System.out);
    }
    
    /**
     * Write the help text on the given {@link PrintStream} instance, even if it has
     * not been requested.
     * 
     * @param stream
     */
    public void showHelp(PrintStream stream) {
    	if(stream == null) {
    		throw new IllegalArgumentException("PrintStream cannot be null");
    	}
    	
    	stream.println(this.getHelpText());
    }
    
    /**
     * Construct the help text for whatever has been requested.
     * 
     * @return
     */
    private String getHelpText() {
        List<String> helpLines = getHelpLines();
        return HelpFormatter.format(helpLines);
    }
    
    /**
     * Create the help lines for the given command.
     * 
     * @return
     */
    private List<String> getHelpLines() {
        if(this.meta.singleCommandMode) {
            return showSingleCommandHelp();
        }
        
        if(result.arguments.isEmpty()) {
            return showHelpSummary();
        }
        
        return showCommandHelp();
    }

    /**
     * Show help for a single command mode.
     * 
     * @return
     */
    private List<String> showSingleCommandHelp() {
        List<String> lines = new ArrayList<>();
        
        lines.add(this.meta.name + ": " + this.meta.description);
        lines.add(null);
        lines.add("Usage:");
        lines.add(getUsageLine());
        lines.add(null);
        lines.addAll(getOptionsSection());
        
        return lines;
    }

    /**
     * Create the help lines for applicable options.
     * 
     * @return
     */
    private List<String> getOptionsSection() {
        List<String> lines = new ArrayList<>();
        
        if(this.meta.globalOptions.isEmpty() && this.meta.commandOptions.isEmpty()) {
        	return lines;
        }
        
        lines.add("Available options:");
        lines.add(null);
        
        // add all global options
        lines.addAll(buildForOptions(this.meta.globalOptions));
        
        // build for group options - only if needed
        if(!this.meta.commandGroups.isEmpty()) {
            
        }
        
        // add all command options
        if(this.result.command != null) {
            lines.addAll(buildForOptions(this.meta.commandOptions.get(this.result.command)));
        }
        
        return lines;
    }

    /**
     * Build the options list.
     * 
     * @param options
     * @return
     */
    private List<String> buildForOptions(Map<String, Option> optionMap) {
        List<String> lines = new ArrayList<>();
        
        if(AssertUtils.isEmpty(optionMap)) {
        	return lines;
        }

        Collection<Option> optionsCollection = optionMap.values();
        
        Set<Option> options = new HashSet<>(optionsCollection);
        for(Option option : options) {
            String[] names = option.name();
            lines.add("\t" + OutlineUtil.join(names, ", "));
            lines.add("\t\t" + option.description());
            lines.add(null);
        }
        
        return lines;
    }

    /**
     * Create the basic header for help lines.
     * 
     * @return
     */
    private List<String> newHelpLines() {
        List<String> lines = new ArrayList<>();
        
        lines.add(this.meta.name);
        lines.add(null);
        lines.add("\t" + this.meta.description);
        lines.add(null);
        lines.add("Usage:");
        lines.add("\t" + getUsageLine());
        lines.add(null);
        
        return lines;
    }

    /**
     * Construct the usage line.
     * 
     * @return
     */
    private String getUsageLine() {
    	StringBuilder builder = new StringBuilder(1024);
    	
    	builder.append('\t');
    	builder.append(this.meta.name);
    	builder.append(' ');
    	
    	// all global options must be shown here
    	if(AssertUtils.isNotEmpty(this.meta.globalOptions)) {
    		boolean first = true;
    		
    		// show one for each of the param
    		Set<Option> options = new HashSet<>(this.meta.globalOptions.values());
    		for(Option option : options) {
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
    					builder.append(" <arg>");
    				} else {
    					builder.append(" <arg" + (index + 1) + ">");
    				}
    			}
    			
    			builder.append(']');
    		}
    	}
    	
    	// then we show the command
    	// this is only applicable for multi-command mode
    	if(!this.meta.singleCommandMode) {
    		builder.append(" <command>");
    	}
    	
    	// and lastly we need to display the arguments that can be passed
    	// this needs to be displayed, only if there are arguments that can be applied
    	// to a command
    	if(this.meta.singleCommandMode) {
    		if(!this.meta.commandHasArguments.isEmpty()) {
    			builder.append(" [<args>]");
    		}
    	} else {
    		builder.append(" [<args>]");
    	}
    	
    	return builder.toString();
    }

    /**
     * Show help for a specific command or group.
     * 
     * @return
     */
    private List<String> showCommandHelp() {
        return null;
    }

    /**
     * Show help summary for multi-command mode.
     * 
     * @return
     */
    private List<String> showHelpSummary() {
    	List<String> lines = new ArrayList<>();
        
        lines.add(this.meta.name + ": " + this.meta.description);
        lines.add(null);
        lines.add("Usage:");
        lines.add(getUsageLine());
        lines.add(null);
        lines.addAll(getOptionsSection());
        lines.add(null);
        lines.addAll(getCommands());
        
        return lines;
    }

    /**
     * Return information on all available commands - this could be list of all
     * commands at global level, or at group level
     * 
     * @return
     */
	private List<String> getCommands() {
		List<String> lines = new ArrayList<>();
		
        lines.add("Available commands:");
        lines.add(null);
        
        if(AssertUtils.isNotEmpty(this.meta.commandNames)) {
        	Set<Command> commands = new HashSet<>(this.meta.commandNames.values());
        	for(Command command : commands) {
        		if(command == null) {
        			// this happens because of the help keyword
        			continue;
        		}
        		
                lines.add("\t" + command.name());
                lines.add("\t\t" + command.description());
                lines.add(null);
        	}
        }
		
		return lines;
	}

}
