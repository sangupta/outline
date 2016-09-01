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

import com.sangupta.outline.Outline;
import com.sangupta.outline.OutlineMetadata;
import com.sangupta.outline.parser.ParseResult;

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
	 * Convenience constructor.
	 * 
	 * @param metadata
	 * @param result
	 */
    public OutlineHelp(OutlineMetadata metadata, ParseResult result) {
        this.meta = metadata;
        this.result = result;
    }

    /**
     * Returns whether help has been requested by the user or not.
     * 
     * @return
     */
    public boolean isHelpRequested() {
		return result.helpRequested;
	}

    /**
     * Display help if it has been requested. Returns <code>true</code> if help was
     * requested, <code>false</code> otherwise.
     * 
     * @return
     */
    public boolean showHelpIfRequested() {
    	if(this.isHelpRequested()) {
    		this.showHelp();
    		return true;
    	}
    	
    	return false;
    }
    
    public String getHelp() {
    	return new OutlineHelpBuilder(meta, result).getHelpText();
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
    	
    	stream.println(new OutlineHelpBuilder(meta, result).getHelpText());
    }

	/**
	 * @return the meta
	 */
	public OutlineMetadata getMeta() {
		return meta;
	}

	/**
	 * @return the result
	 */
	public ParseResult getResult() {
		return result;
	}
    
}
