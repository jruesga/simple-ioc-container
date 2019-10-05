package com.ruesga.siocc.resolvers;

/**
 * An exception thrown when a class is not instantiable.
 */
public class NonInstantiableClassException extends DependencyResolutionException {
    private final Class<?> clazz;

    /**
     * Creates a new {@link NonInstantiableClassException} reference.
     *
     * @param clazz the class that isn't instantiable.
     */
    public NonInstantiableClassException(Class<?> clazz) {
        super(String.format("Class of type %s is not instantiable", clazz.getName()));
        this.clazz = clazz;
    }

    /**
     * Returns the class that isn't instantiable.
     *
     * @return the class.
     */
    public Class<?> clazz() {
        return this.clazz;
    }
}
