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
import java.util.List;
import java.util.Map;

import com.sangupta.outline.OutlineMetadata;
import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.parser.ParseResult;
import com.sangupta.outline.util.OutlineUtil;

@Command(name = "help", description = "Display help information")
public class OutlineHelpCommand {
    
    protected final OutlineMetadata meta;
    
    protected final ParseResult result;

    public OutlineHelpCommand(OutlineMetadata metadata, ParseResult result) {
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
        List<String> lines = newHelpLines();
        
        lines.add(this.meta.name);
        lines.add(null);
        lines.add("\t" + this.meta.description);
        lines.add(null);
        lines.add("usage: " + getUsage());
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

        Collection<Option> options = optionMap.values();
        for(Option option : options) {
            String[] names = option.name();
            lines.add("\t" + OutlineUtil.join(names, ", "));
            lines.add("\t\t" + option.description());
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
        return null;
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
        return null;
    }

}
