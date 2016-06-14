package com.sangupta.outline;

import java.lang.reflect.Field;

public interface OutlineTypeConverter<T> {
    
    public T convertFrom(Field field, Object instance, Object value);

}
