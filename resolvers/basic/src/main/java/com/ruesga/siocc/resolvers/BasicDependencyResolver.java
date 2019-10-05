package com.ruesga.siocc.resolvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link DependencyResolver} implementation able to map single classes, implementation classes and factories
 * in order to resolve dependencies.
 * <p>
 * In order to create a new {@link BasicDependencyResolver} reference, developers must use the {@link Builder} class.
 *
 * @see DependencyResolver
 * @see Builder
 */
public class BasicDependencyResolver implements DependencyResolver {
    private final static Logger logger = LoggerFactory.getLogger(BasicDependencyResolver.class);

    /**
     * A class to provide classes.
     *
     * @param <T> the type of class to provide.
     */
    public interface Provider<T> {
        /**
         * Returns a new reference of the type
         *
         * @return a new reference
         */
        T provide();
    }

    /**
     * A builder for creating {@link BasicDependencyResolver} classes.
     */
    public static class Builder {
        private BasicDependencyResolver resolver;

        /**
         * Creates a new {@link BasicDependencyResolver.Builder} reference.
         */
        public Builder() {
            this.resolver = new BasicDependencyResolver();
        }

        /**
         * Registers a new single class.
         *
         * @param impl the implementation type.
         * @return the own builder's reference.
         * @throws NonInstantiableClassException if the passed implementation type class cannot be instantiated.
         */
        public Builder register(Class<?> impl) throws NonInstantiableClassException {
            if (impl.isInterface() || Modifier.isAbstract(impl.getModifiers())) {
                throw new NonInstantiableClassException(impl);
            }
            this.resolver.mapping.put(impl, impl);
            return this;
        }

        /**
         * Registers a new implementation class.
         *
         * @param type the base type.
         * @param impl the implementation type.
         * @param <T> the class' type.
         * @return the own builder's reference.
         * @throws NonInstantiableClassException if the passed implementation type class cannot be instantiated.
         */
        public <T> Builder register(Class<T> type, Class<? extends T> impl) throws NonInstantiableClassException {
            if (impl.isInterface() || Modifier.isAbstract(impl.getModifiers())) {
                throw new NonInstantiableClassException(impl);
            }
            this.resolver.mapping.put(type, impl);
            return this;
        }

        /**
         * Registers a new factory class.
         *
         * @param type the base type.
         * @param provider the implementation factory.
         * @param <T> the class' type.
         * @return the own builder's reference.
         */
        public <T> Builder register(Class<T> type, Provider<T> provider) {
            this.resolver.mapping.put(type, provider);
            return this;
        }

        /**
         * Creates and returns a new {@link BasicDependencyResolver} reference.
         *
         * @return a new {@link BasicDependencyResolver} reference.
         */
        public BasicDependencyResolver build() {
            return resolver;
        }
    }

    private final Map<Class<?>, Object> mapping = new HashMap<>();

    private BasicDependencyResolver() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        if (!mapping.containsKey(type)) {
            return null;
        }
        Object o = mapping.get(type);
        if (o instanceof Provider) {
            return (T)((Provider) o).provide();
        }
        if (o instanceof Class) {
            try {
                return (T) ((Class) o).newInstance();
            } catch (ReflectiveOperationException e) {
                logger.warn("Can't create a new instance of type '{}' with implementation '{}'. " +
                                "Does it have a public empty constructor?",
                        type.getName(), ((Class) o).getName());
            }
        }
        return null;
    }
}
