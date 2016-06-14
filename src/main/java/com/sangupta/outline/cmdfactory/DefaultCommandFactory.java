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
