package com.ruesga.siocc.resolvers;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link DependencyResolver} implementation that scans the classpath to look for annotated classes with
 * the {@link IoCDependency} annotation.
 * <p>
 * In order to create a new {@link AnnotationDependencyResolver} reference, developers must use
 * the {@link Builder} class.
 *
 * @see DependencyResolver
 * @see Builder
 * @see IoCDependency
 */
public class AnnotationDependencyResolver implements DependencyResolver {
    private final static Logger logger = LoggerFactory.getLogger(AnnotationDependencyResolver.class);
    /**
     * A builder for creating {@link AnnotationDependencyResolver} classes.
     */
    public static class Builder {
        private AnnotationDependencyResolver resolver;
        private String[] pkgs = {};

        /**
         * Creates a new {@link AnnotationDependencyResolver.Builder} reference.
         */
        public Builder() {
            this.resolver = new AnnotationDependencyResolver();
        }

        /**
         * Sets the packages where to scan for annotations.
         *
         * @param pkgs the packages.
         * @return the own builder's reference.
         */
        public Builder scan(String... pkgs) {
            this.pkgs = pkgs;
            return this;
        }

        /**
         * Creates and returns a new {@link AnnotationDependencyResolver} reference.
         *
         * @return a new {@link AnnotationDependencyResolver} reference.
         */
        @SuppressWarnings("RedundantCast")
        public AnnotationDependencyResolver build() {
            Reflections reflections = new Reflections((Object[]) pkgs);
            resolver.dependencies = reflections.getTypesAnnotatedWith(IoCDependency.class);
            return resolver;
        }
    }

    private Set<Class<?>> dependencies;

    private AnnotationDependencyResolver() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        Optional<T> o =
                dependencies.stream().map(c -> {
                    try {
                        if (c.equals(type)) {
                            return (T) c.newInstance();
                        }
                        if (type.isAssignableFrom(c)) {
                            return (T) c.newInstance();
                        }
                    } catch (ReflectiveOperationException ex) {
                        logger.warn("Can't create a new instance of type '{}' with implementation '{}'. " +
                                        "Does it have a public empty constructor?",
                                type.getName(), ((Class) c).getName());
                    }
                    return (T) null;
                })
                .filter(Objects::nonNull)
                .findFirst();
        return o.orElse(null);
    }
}
