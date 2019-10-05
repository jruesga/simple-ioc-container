package com.ruesga.siocc;

/**
 * A configuration class for customize {@link IoC#resolve(Class)}'s behavior.
 * <p>
 * In order to create a new {@link Configuration} reference, developers must use the {@link Builder} class.
 *
 * @see IoC
 * @see Builder
 */
public class Configuration {
    public enum NoDependencyFoundStrategy {NULL, THROW}
    public enum CircularDependencyStrategy {NULL, THROW}
    public enum NonAccessibleFieldStrategy {NULL, THROW}

    /**
     * A builder for creating {@link Configuration} classes.
     */
    public static class Builder {
        private Configuration configuration;

        /**
         * Creates a new {@link Configuration.Builder} reference.
         */
        public Builder () {
            this.configuration = new Configuration();
        }

        /**
         * Configures the strategy to used when a dependency wasn't be resolved.
         *
         * @param strategy the strategy to use.
         * @return the own builder's reference.
         * @see NoDependencyFoundStrategy
         */
        public Builder noDependencyFoundStrategy(NoDependencyFoundStrategy strategy) {
            this.configuration.noDependencyFoundStrategy = strategy;
            return this;
        }

        /**
         * Configures the strategy to used when a circular dependency was detected.
         *
         * @param strategy the strategy to use.
         * @return the own builder's reference.
         * @see CircularDependencyStrategy
         */
        public Builder circularDependencyStrategy(CircularDependencyStrategy strategy) {
            this.configuration.circularDependencyStrategy = strategy;
            return this;
        }

        /**
         * Configures the strategy to used when a injectable field wasn't accessible.
         *
         * @param strategy the strategy to use.
         * @return the own builder's reference.
         * @see NonAccessibleFieldStrategy
         */
        public Builder nonAccessibleFieldStrategy(NonAccessibleFieldStrategy strategy) {
            this.configuration.nonAccessibleFieldStrategy = strategy;
            return this;
        }

        /**
         * Creates and returns a new {@link Configuration} reference.
         *
         * @return a new {@link Configuration} reference.
         */
        public Configuration build() {
            return configuration;
        }
    }

    private NoDependencyFoundStrategy noDependencyFoundStrategy = NoDependencyFoundStrategy.NULL;
    private CircularDependencyStrategy circularDependencyStrategy = CircularDependencyStrategy.NULL;
    private NonAccessibleFieldStrategy nonAccessibleFieldStrategy = NonAccessibleFieldStrategy.NULL;

    private Configuration() {
    }

    NoDependencyFoundStrategy noDependencyFoundStrategy() {
        return this.noDependencyFoundStrategy;
    }

    CircularDependencyStrategy circularDependencyStrategy() {
        return this.circularDependencyStrategy;
    }

    NonAccessibleFieldStrategy nonAccessibleFieldStrategy() {
        return this.nonAccessibleFieldStrategy;
    }
}
