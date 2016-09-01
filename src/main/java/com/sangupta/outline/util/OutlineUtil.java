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
 
package com.sangupta.outline.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.outline.Outline;

/**
 * Utility classes for {@link Outline} framework.
 * 
 * @author sangupta
 *
 */
public class OutlineUtil {
    
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        populateAllFields(clazz, fields);
        return fields;
    }
    
    private static void populateAllFields(Class<?> clazz, List<Field> fields) {
        if(clazz == null) {
            return;
        }
        
        Field[] array = clazz.getDeclaredFields();
        if(array != null && array.length > 0) {
            fields.addAll(Arrays.asList(array));
        }
        
        if(clazz.getSuperclass() == null) {
            return;
        }
        
        populateAllFields(clazz.getSuperclass(), fields);
    }

    public static String join(String[] str, String joiner) {
    	if(AssertUtils.isEmpty(str)) {
    		return "";
    	}
    	
    	StringBuilder builder = new StringBuilder();
        
    	final boolean hasJoiner = AssertUtils.isNotEmpty(joiner);
        boolean first = true;
        for(String s : str) {
            if(hasJoiner && !first) {
                builder.append(joiner);
            }
            
            builder.append(s);
            first = false;
        }
        
        return builder.toString();
    }

    public static String ltrim(String s) {
    	if(AssertUtils.isEmpty(s)) {
    		return "";
    	}
    	
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }
    
}
