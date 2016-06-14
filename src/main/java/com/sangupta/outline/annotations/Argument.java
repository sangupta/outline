package com.sangupta.outline.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to capture one single argument at a time.
 * 
 * @author sangupta
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Argument {
    
    int order() default 0;
    
    /**
     * Title for the argument - by which it will show up in help
     */
    String title() default "";

    /**
     * A description of the argument - details on what it does
     */
    String description() default "";

    /**
     * Argument usage for help.
     */
    String usage() default "";

    /**
     * Whether this arguments are required.
     */
    boolean required() default false;

}
