package com.ruesga.siocc.resolvers;

import java.lang.reflect.Field;

/**
 * An exception thrown when a field cannot be injected because it is inaccessible.
 */
public class NonAccessibleFieldException extends DependencyResolutionException {
    private final Field field;

    /**
     * Creates a new {@link NonAccessibleFieldException} reference.
     *
     * @param field the inaccessible field.
     */
    public NonAccessibleFieldException(Field field) {
        super(String.format("Field %s of type %s is not accessible", field.getName(), field.getClass().getName()));
        this.field = field;
    }

    /**
     * Returns the inaccessible field.
     *
     * @return the inaccessible field.
     */
    public Field field() {
        return this.field;
    }
}
