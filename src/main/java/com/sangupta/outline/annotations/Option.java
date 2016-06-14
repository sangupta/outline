package com.sangupta.outline.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Option {
    
    /**
     * Is this a command, group or global option
     */
    OptionType type() default OptionType.COMMAND;

    /**
     * Name use to identify the option value in documentation and error messages.
     */
    String title() default "";

    /**
     * An array of allowed command line parameters (e.g. "-n", "--name", etc...).
     */
    String[] name();

    /**
     * A description of this option.
     */
    String description() default "";

    /**
     * Whether this option is required.
     */
    boolean required() default false;

    /**
     * How many parameter values this option will consume. For example,
     * an arity of 2 will allow "-pair value1 value2".
     */
    int arity() default Integer.MIN_VALUE;

    /**
     * If true, this parameter won't appear in the usage().
     */
    boolean hidden() default false;

    String[] allowedValues() default {};

}
