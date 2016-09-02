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

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.StringUtils;
import com.sangupta.outline.util.OutlineUtil;

/**
 * This class helps us write an indented string that breaks at a given length
 * without the callee being concerned over the line length.
 * 
 * @author sangupta
 *
 */
public class IndentedStringWriter {
	
	private final StringBuilder builder = new StringBuilder(10 * 1024); // initialize with 10kb of character space

	/**
	 * The line length to break at
	 */
	private final int lineLength;
	
	/**
	 * Replace tabs to 4 spaces 
	 */
	private final int tabLength = 4; 
	
	/**
	 * The current indentation level
	 */
	private int indentLevel = 0;
	
	/**
	 * The current pointer in the line - specifies how
	 * many number of characters have already been written to the line.
	 */
	private int currentPointer = 0;
	
	public IndentedStringWriter() {
		this(100); // default line length is 60 chars
	}

	public IndentedStringWriter(int lineLength) {
		this.lineLength = lineLength;
	}
	
	public void setIndentLevel(int indentLevel) {
		this.indentLevel = indentLevel;
		this.newLine();
	}
	
	public void incrementIndent() {
		this.indentLevel++;
		this.newLine();
	}
	
	public void decrementIndent() {
		this.indentLevel--;
		this.newLine();
	}
	
	public void write(char ch) {
		this.write(String.valueOf(ch));
	}
	
	/**
	 * Write the given string fragment to current line.
	 * 
	 * @param str
	 */
	public void write(String str) {
		String[] tokens = str.split("\n");
		for(int index = 0; index < tokens.length; index++) {
			if(index > 1) {
				this.newLine();
			}
			
			this.writeInternal(tokens[index]);
		}
	}
	
	private void writeInternal(String str) {
		if(AssertUtils.isEmpty(str)) {
			return;
		}
		
		int len = str.length();
		boolean breakNeeded = this.currentPointer + str.length() > this.lineLength;
		if(!breakNeeded) {
			this.builder.append(str);
			this.currentPointer += len;
			return;
		}
		
		// add the prefix - whatever we can achieve in the remaining space
		int breakPoint = this.lineLength - this.currentPointer;
		
		// check if break point is not breaking a word
		for(int index = breakPoint; index > 0; index--) {
			if(Character.isWhitespace(str.charAt(index))) {
				breakPoint = index;
				break;
			}
		}
		
		// add the prefix
		String prefix = str.substring(0, breakPoint);
		this.builder.append(prefix);
		this.newLine();
		
		// call recursive
		if(breakPoint == 0) {
			this.builder.append(str);
			this.currentPointer = str.length();
			return;
		}
		try {
			this.write(OutlineUtil.ltrim(str.substring(breakPoint)));
		} catch(StackOverflowError e) {
			System.out.println("breakpoint: " + breakPoint);
			System.out.println("string: " + str);
			System.exit(0);
		}
	}
	
	/**
	 * Write the given string fragment to current line and start a new line
	 * at the end.
	 * 
	 * @param str
	 */
	public void writeLine(String str) {
		this.write(str);
		this.newLine();
	}
	
	/**
	 * Start a new indented line.
	 * 
	 */
	public void newLine() {
		this.builder.append('\n');
		this.currentPointer = 0;
		
		if(this.indentLevel == 0) {
			return;
		}
		
		int chars = this.indentLevel * this.tabLength;
		this.builder.append(StringUtils.repeat(' ', chars));
		this.currentPointer = chars;
	}
	
	/**
	 * Return the current string representation.
	 * 
	 */
	public String getString() {
		return this.builder.toString();
	}
	
	@Override
	public String toString() {
		return this.getString();
	}
}
