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
 
package com.sangupta.outline.parser;

import java.util.Iterator;

class StringArrayIterator implements Iterator<String> {
    
    private final String[] items;
    
    private int position = 0;
    
    public StringArrayIterator(String[] items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        if(position < items.length) {
            return true;
        }
        
        return false;
    }

    @Override
    public String next() {
        return this.items[this.position++];
    }
    
    public String peek() {
        if(!this.hasNext()) {
            return null;
        }
        
        return this.items[this.position];
    }
    
}
