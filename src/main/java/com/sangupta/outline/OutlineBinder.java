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
 
package com.sangupta.outline;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.ds.SimpleMultiMap;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ReflectionUtils;
import com.sangupta.outline.annotations.Argument;
import com.sangupta.outline.annotations.Arguments;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.annotations.OptionType;
import com.sangupta.outline.exceptions.OutlineRequiredOptionMissingException;
import com.sangupta.outline.parser.ParseResult;
import com.sangupta.outline.util.OutlineUtil;

/**
 * Bind functions that bind the command {@link Object} instance to the
 * values that are received and parsed for this specific command.
 * 
 * @author sangupta
 *
 */
class OutlineBinder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OutlineBinder.class);
    
    final static Map<Class<?>, OutlineTypeConverter<?>> converters = new HashMap<>();
    
    static <T> void registerTypeConverter(Class<T> classOfT, OutlineTypeConverter<T> converter) {
        converters.put(classOfT, converter);
    }

    /**
     * Bind the resultant properties that we have got to the object instance. So that
     * the instance is ready-for-use.
     * 
     * @param instance
     * @param result
     */
    public static void bindInstanceToProperties(Class<?> clazz, Object instance, ParseResult result) {
        List<Field> fields = OutlineUtil.getAllFields(clazz);
        bindAllOptions(fields, instance, result);
        
        int startOrder = bindAllArgumentsWithOrder(fields, instance, result);
        
        bindRemainingArguments(fields, instance, result, startOrder);
    }

    /**
     * Bind all the remaining arguments that havae not yet been set using <code>@Argument</code> annotation
     * to any field that requires <code>@Arguments</code> annotation.
     *  
     * @param fields
     * @param instance
     * @param result
     * @param startOrder
     */
    private static void bindRemainingArguments(List<Field> fields, Object instance, ParseResult result, int startOrder) {
        List<String> remaining;
        if(startOrder > 0) {
            remaining = result.arguments.subList(startOrder, result.arguments.size());
        } else {
            remaining = result.arguments;
        }
        
        for(Field field : fields) {
            Arguments arguments = field.getAnnotation(Arguments.class);
            if(arguments == null) {
                continue;
            }

            bindValueToField(field, instance, remaining);
            return;
        }
    }

    private static int bindAllArgumentsWithOrder(List<Field> fields, Object instance, ParseResult result) {
        int maxOrderRead = -1;
        
        for(Field field : fields) {
            Argument argument = field.getAnnotation(Argument.class);
            if(argument == null) {
                continue;
            }
            
            int order = argument.order();
            if(order >= result.arguments.size()) {
                if(argument.required()) {
                    throw new RuntimeException("missing mandatory argument param");
                }
                
                continue;
            }
            
            maxOrderRead = Math.max(maxOrderRead, order);
            
            bindValueToField(field, instance, result.arguments.get(order));
        }
        
        return maxOrderRead + 1;
    }

    private static void bindAllOptions(List<Field> fields, Object instance, ParseResult result) {
        for(Field field : fields) {
            // check for @Option annotation
            Option option = field.getAnnotation(Option.class);
            if(option == null) {
                continue;
            }

            String[] names = option.name();
            OptionType type = option.type();
            List<String> values = null;
            switch(type) {
                case COMMAND:
                    values = getOptionValues(result.commandOptions, names);
                    break;
                    
                case GLOBAL:
                    values = getOptionValues(result.globalOptions, names);
                    break;
                    
                case GROUP:
                    values = getOptionValues(result.groupOptions, names);
                    break;
                    
                default:
                    break;
            }
            
            if(option.required() && AssertUtils.isEmpty(values)) {
                throw new OutlineRequiredOptionMissingException(option.name()[0]);
            }
            
            bindValueToField(field, instance, values);
        }
    }

    private static void bindValueToField(Field field, Object instance, Object value) {
        if(value instanceof List<?>) {
            List<?> values = (List<?>) value;
            
            if(values.isEmpty()) {
                ReflectionUtils.bindValueQuiet(field, instance, null);
                return;
            }
            
            if(values.size() == 1) {
                bindValueToField(field, instance, values.get(0));
                return;
            }
        }
        
        Class<?> fieldClass = field.getType();
        if(converters.containsKey(fieldClass)) {
            value = converters.get(fieldClass).convertFrom(field, instance, value);
        }
        
        try {
            ReflectionUtils.bindValue(field, instance, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOGGER.warn("Unable to set field value via reflection", e);
        }
    }

    private static List<String> getOptionValues(SimpleMultiMap<String, String> options, String[] names) {
        List<String> values = new ArrayList<>();
        
        for(String name : names) {
            List<String> list = options.getValues(name);
            if(list == null || list.isEmpty()) {
                continue;
            }
            
            values.addAll(list);
        }
        
        return values;
    }
}
