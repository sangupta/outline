package com.sangupta.outline;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sangupta.jerry.ds.SimpleMultiMap;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ReflectionUtils;
import com.sangupta.outline.annotations.Argument;
import com.sangupta.outline.annotations.Arguments;
import com.sangupta.outline.annotations.Option;
import com.sangupta.outline.annotations.OptionType;
import com.sangupta.outline.parser.ParseResult;

/**
 * Bind functions that bind the command {@link Object} instance to the
 * values that are received and parsed for this specific command.
 * 
 * @author sangupta
 *
 */
public class OutlineBinder {

    /**
     * Bind the resultant properties that we have got to the object instance. So that
     * the instance is ready-for-use.
     * 
     * @param instance
     * @param result
     */
    public static void bindInstanceToProperties(Class<?> clazz, Object instance, ParseResult result) {
        Field[] fields = clazz.getDeclaredFields();
        bindAllOptions(fields, instance, result);
        
        int startOrder = bindAllArgumentsWithOrder(fields, instance, result);
        
        bindRemainingArguments(fields, instance, result, startOrder);
    }

    private static void bindRemainingArguments(Field[] fields, Object instance, ParseResult result, int startOrder) {
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

    private static int bindAllArgumentsWithOrder(Field[] fields, Object instance, ParseResult result) {
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

    private static void bindAllOptions(Field[] fields, Object instance, ParseResult result) {
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
                throw new RuntimeException("Required param not available");
            }
            
            bindValueToField(field, instance, values);
        }
    }

    private static void bindValueToField(Field field, Object instance, List<String> values) {
        try {
            ReflectionUtils.bindValue(field, instance, values);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void bindValueToField(Field field, Object instance, String string) {
        try {
            ReflectionUtils.bindValue(field, instance, string);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
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
