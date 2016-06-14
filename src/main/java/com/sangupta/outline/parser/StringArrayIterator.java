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
