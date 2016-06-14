package com.sangupta.outline.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Arguments {
    
    /**
     * Name of the arguments.
     */
    String title() default "";

    /**
     * A description of the arguments.
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
