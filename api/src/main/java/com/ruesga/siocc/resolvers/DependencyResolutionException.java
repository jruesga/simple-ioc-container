package com.ruesga.siocc.resolvers;

/**
 * A base class for all <i>dependency resolution</i> exceptions.
 */
public abstract class DependencyResolutionException extends Exception {
    /**
     * {@inheritDoc}
     */
    public DependencyResolutionException() {
    }

    /**
     * {@inheritDoc}
     */
    public DependencyResolutionException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public DependencyResolutionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public DependencyResolutionException(Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    public DependencyResolutionException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
