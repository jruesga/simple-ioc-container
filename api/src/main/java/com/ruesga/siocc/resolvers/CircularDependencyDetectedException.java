package com.ruesga.siocc.resolvers;

/**
 * An exception thrown when a circular dependency is detected.
 */
public class CircularDependencyDetectedException extends DependencyResolutionException {
    private final Class<?> type;

    /**
     * Creates a new {@link CircularDependencyDetectedException} reference.
     *
     * @param type the class' type in which the circular dependency was detected
     */
    public CircularDependencyDetectedException(Class<?> type) {
        super(String.format("A circular dependency was found in type: %s", type.getName()));
        this.type = type;
    }

    /**
     * Returns the class' type in which the circular dependency was detected
     *
     * @return the class' type.
     */
    public Class<?> type() {
        return this.type;
    }
}
