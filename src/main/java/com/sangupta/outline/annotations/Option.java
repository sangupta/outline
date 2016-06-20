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
    int arity() default 1;

    /**
     * If true, this parameter won't appear in the usage().
     */
    boolean hidden() default false;

    String[] allowedValues() default {};

}
