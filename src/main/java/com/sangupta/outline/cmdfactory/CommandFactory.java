package com.sangupta.outline.cmdfactory;

public interface CommandFactory {

    public <T> T createInstance(Class<T> instanceClass);
    
}
