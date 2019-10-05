package com.ruesga.siocc.resolvers;

/**
 * An exception thrown when no dependency was resolved.
 */
public class NoDependencyResolvedException extends DependencyResolutionException {
    private final Class<?> type;

    /**
     * Creates a new {@link NoDependencyResolvedException} reference.
     *
     * @param type the unresolved class' type.
     */
    public NoDependencyResolvedException(Class<?> type) {
        super(String.format("No dependency resolved for type: %s", type.getName()));
        this.type = type;
    }

    /**
     * Returns the unresolved class' type.
     *
     * @return the unresolved class' type.
     */
    public Class<?> type() {
        return this.type;
    }
}
