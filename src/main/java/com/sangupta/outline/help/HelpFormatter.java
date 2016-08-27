package com.sangupta.outline.help;

import java.util.List;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.StringUtils;

public class HelpFormatter {

    public static String format(List<String> helpLines) {
    	if(AssertUtils.isEmpty(helpLines)) {
    		return StringUtils.EMPTY_STRING;
    	}
    	
        StringBuilder builder = new StringBuilder(256 * helpLines.size());
        
        for(String line : helpLines) {
        	if(line != null) {
        		builder.append(line);
        	}
        	
            builder.append(StringUtils.SYSTEM_NEW_LINE);
        }
        
        return builder.toString();
    }

}
