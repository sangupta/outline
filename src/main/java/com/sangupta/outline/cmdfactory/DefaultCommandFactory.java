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
 
package com.sangupta.outline.cmdfactory;

public class DefaultCommandFactory implements CommandFactory {

    @Override
    public <T> T createInstance(Class<T> instanceClass) {
        try {
            return instanceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // TODO: fix this
            throw new RuntimeException("Unable to instantiate command class", e);
        }
    }

}
