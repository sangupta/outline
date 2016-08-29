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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sangupta.jerry.util.AssertUtils;
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
    
    protected final OutlineMetadata meta;
    
    protected final ParseResult result;

    public OutlineHelp(OutlineMetadata metadata, ParseResult result) {
        this.meta = metadata;
        this.result = result;
    }

    /**
     * Show the help as requested on console.
     * 
     */
    public void showHelp() {
        List<String> helpLines = getHelpLines();
        System.out.println(HelpFormatter.format(helpLines));
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
        lines.add(getUsage());
        lines.add(null);
        lines.addAll(getOptions());
        
        return lines;
    }

    /**
     * Create the help lines for applicable options.
     * 
     * @return
     */
    private List<String> getOptions() {
        List<String> lines = new ArrayList<>();
        
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
        lines.add("\t" + getUsage());
        lines.add(null);
        
        return lines;
    }

    /**
     * Construct the usage line.
     * 
     * @return
     */
    private String getUsage() {
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
    	builder.append(" <command>");
    	
    	// and lastly we need to display the arguments that can be passed
    	builder.append(" [<args>]");
    	
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
        lines.add(getUsage());
        lines.add(null);
        lines.addAll(getOptions());
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
