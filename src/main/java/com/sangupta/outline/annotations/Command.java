package com.sangupta.outline.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Command {
    
    /**
     * Name of the command.
     */
    String name();

    /**
     * Description of the command.
     */
    String description() default "";
    
    /**
     * The command group to which this command belongs
     */
    String group() default "";

    /**
     * If true, this command won't appear in the usage().
     */
    boolean hidden() default false;

}
