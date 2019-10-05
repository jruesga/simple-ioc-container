package com.ruesga.siocc;

import com.ruesga.siocc.resolvers.CircularDependencyDetectedException;
import com.ruesga.siocc.resolvers.DependencyResolutionException;
import com.ruesga.siocc.resolvers.DependencyResolver;
import com.ruesga.siocc.resolvers.NoDependencyResolvedException;
import com.ruesga.siocc.resolvers.NonAccessibleFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * A simple <b><i>Inversion of Control</i></b> dependency container.
 * <p>
 * An {@link IoC} reference can be obtained by calling one of {@link #create(DependencyResolver...)} or
 * {@link #create(Configuration, DependencyResolver...)} methods.
 * <p>
 * Several {@link DependencyResolver} implementations can be provided in order to resolve dependencies.
 * A {@link Configuration} class can also be provided to change the behaviour of {@link #resolve(Class)}
 * method.
 * <p>
 * An example of a basic usage of this class is described below:
 * <pre>
 *     Configuration configuration = new Configuration.Builder()
 *         .circularDependencyStrategy(CircularDependencyStrategy.THROW)
 *         .build();
 *     BasicDependencyResolver resolver =
 *                 new BasicDependencyResolver.Builder()
 *                         .register(Service.class, ServiceImpl.class)
 *                         .build();
 *     IoC container = IoC.create(configuration, resolver);
 *     Service service = container.resolve(Service.class);
 *     service.callMethod();
 * </pre>
 *
 * @see DependencyResolver
 * @see Configuration
 */
public class IoC {
    private final static Logger logger = LoggerFactory.getLogger(IoC.class);

    private final Configuration configuration;
    private final List<DependencyResolver> resolvers;

    /**
     * Creates a new dependency container using the passed {@link DependencyResolver}s and using
     * the default {@link Configuration}.
     *
     * @param resolvers the list of {@link DependencyResolver}s implementations used to resolve types.
     * @return A new container reference.
     * @see DependencyResolver
     * @see Configuration
     */
    public static IoC create(DependencyResolver... resolvers) {
        return create(new Configuration.Builder().build(), resolvers);
    }

    /**
     * Creates a new dependency container using the passed {@link DependencyResolver}s and {@link Configuration}.
     *
     * @param configuration the configuration used to determine the behaviour of the {@link IoC#resolve(Class)} method.
     * @param resolvers the list of {@link DependencyResolver}s implementations used to resolve types.
     * @return A new container reference.
     * @see DependencyResolver
     * @see Configuration
     */
    public static IoC create(Configuration configuration, DependencyResolver... resolvers) {
        return new IoC(configuration, resolvers);
    }

    private IoC(Configuration configuration, DependencyResolver... resolvers) {
        this.configuration = configuration;
        this.resolvers = Arrays.asList(resolvers);
    }

    /**
     * Resolves the passed type as argument and returns an instance of the resolved type.
     *
     * @param type the desired type.
     * @param <T> the class type of the desired type.
     * @return an instance of the resolved type or <code>null</code> if not resolved.
     * @throws DependencyResolutionException if something when wrong trying to resolve the desired type.
     */
    public <T> T resolve(Class<T> type) throws DependencyResolutionException {
        Stack<Class<?>> dependencies = new Stack<>();
        return resolveInternal(type, dependencies);
    }

    private <T> T resolveInternal(Class<T> type, Stack<Class<?>> dependencies)
            throws DependencyResolutionException {
        // Resolve the type with one of the resolvers
        Optional<T> o = resolvers.stream()
                .map(r -> r.resolve(type))
                .filter(Objects::nonNull)
                .findFirst();

        // If we were able to resolve the type, just try to resolve its field annotated with @Inject
        if (o.isPresent()) {
            // Save the type to check later for circular dependencies
            dependencies.push(type);

            // Get the implementation class
            T x = o.get();

            // Inject every field annotated with @Inject
            List<Field> fields = getInjectAnnotatedFields(x.getClass());
            for (Field field : fields) {
                // Check for circular dependencies
                if (dependencies.contains(field.getType())) {
                    switch (configuration.circularDependencyStrategy()) {
                        case THROW:
                            throw new CircularDependencyDetectedException(dependencies.peek());
                        case NULL:
                        default:
                    }
                    continue;
                }

                // Resolve the field and save it with reflection into the parent object
                Object z = resolveInternal(field.getType(), dependencies);
                field.setAccessible(true);
                try {
                    field.set(x, z);
                } catch (IllegalAccessException e) {
                    // We cannot access the field. Just apply the configured strategy
                    switch (configuration.nonAccessibleFieldStrategy()) {
                        case THROW:
                            throw new NonAccessibleFieldException(field);
                        case NULL:
                        default:
                    }
                }
            }
            return x;
        }

        // We didn't resolve the type. Just apply the configured strategy
        switch (configuration.noDependencyFoundStrategy()) {
            case THROW:
                throw new NoDependencyResolvedException(type);
            case NULL:
            default:
                return null;
        }
    }

    private List<Field> getInjectAnnotatedFields(Class clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.getAnnotation(Inject.class) != null)
                .collect(Collectors.toList());
    }
}
