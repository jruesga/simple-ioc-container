package com.ruesga.siocc;

import com.ruesga.siocc.resolvers.CircularDependencyDetectedException;
import com.ruesga.siocc.resolvers.DependencyResolver;
import com.ruesga.siocc.resolvers.NoDependencyResolvedException;
import com.ruesga.siocc.resolvers.NonAccessibleFieldException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(JUnit4.class)
public class IoCTest {
    private static class TestDependencyResolver implements DependencyResolver {
        private final Map<Class<?>, Class<?>> dependencies = new HashMap<>();

        @SuppressWarnings("unchecked")
        TestDependencyResolver(Pair<Class, Class>... dependencies) {
            Arrays.stream(dependencies).forEach(d -> this.dependencies.put(d.getKey(), d.getValue()));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T resolve(Class<T> type) {
            if (dependencies.containsKey(type)) {
                try {
                    return (T) dependencies.get(type).newInstance();
                } catch (ReflectiveOperationException e) {
                    // ignore
                }
            }
            return null;
        }
    }

    public static class A {
        @Inject private B b;
        private C c;
    }

    public static class A_bad {
        @Inject private B b;
        @Inject private C c;
    }

    public static class B {
    }

    public static class C {
        @Inject private A_bad a;
    }

    public static class D {
    }

    public static class E {
        @Inject private final B b = null;
    }

    @SuppressWarnings("unchecked")
    private static TestDependencyResolver createDependencyResolver() {
        return new TestDependencyResolver(
                new Pair<>(A.class, A.class),
                new Pair<>(A_bad.class, A_bad.class),
                new Pair<>(B.class, B.class),
                new Pair<>(C.class, C.class),
                new Pair<>(E.class, E.class));
    }

    @Test
    public void testResolve() throws Exception {
        IoC container = IoC.create(createDependencyResolver());
        A a = container.resolve(A.class);
        assertThat(a, notNullValue());
        assertThat(a, instanceOf(A.class));
        assertThat(a.b, notNullValue());
        assertThat(a.b, instanceOf(B.class));
        assertThat(a.c, nullValue());
    }

    @Test
    public void testUnresolvedDependencyNullStrategy() throws Exception {
        Configuration configuration = new Configuration.Builder()
                .noDependencyFoundStrategy(Configuration.NoDependencyFoundStrategy.NULL)
                .build();
        IoC container = IoC.create(configuration, createDependencyResolver());
        D d = container.resolve(D.class);
        assertThat(d, nullValue());
    }

    @Test(expected = NoDependencyResolvedException.class)
    public void testUnresolvedDependencyThrowStrategy() throws Exception {
        Configuration configuration = new Configuration.Builder()
                .noDependencyFoundStrategy(Configuration.NoDependencyFoundStrategy.THROW)
                .build();
        IoC container = IoC.create(configuration, createDependencyResolver());
        container.resolve(D.class);
        Assert.fail();
    }

    @Test
    public void testCircularDependencyNullStrategy() throws Exception {
        Configuration configuration = new Configuration.Builder()
                .circularDependencyStrategy(Configuration.CircularDependencyStrategy.NULL)
                .build();
        IoC container = IoC.create(configuration, createDependencyResolver());
        A_bad x = container.resolve(A_bad.class);
        assertThat(x, notNullValue());
        assertThat(x.b, notNullValue());
        assertThat(x.c, notNullValue());
        assertThat(x.c.a, nullValue());
    }

    @Test(expected = CircularDependencyDetectedException.class)
    public void testCircularDependencyThrowStrategy() throws Exception {
        Configuration configuration = new Configuration.Builder()
                .circularDependencyStrategy(Configuration.CircularDependencyStrategy.THROW)
                .build();
        IoC container = IoC.create(configuration, createDependencyResolver());
        container.resolve(A_bad.class);
        Assert.fail();
    }

    @Test
    @Ignore("JVM is not throwing IllegalAccessException")
    public void testNonAccessibleFieldNullStrategy() throws Exception {
        Configuration configuration = new Configuration.Builder()
                .nonAccessibleFieldStrategy(Configuration.NonAccessibleFieldStrategy.NULL)
                .build();
        IoC container = IoC.create(configuration, createDependencyResolver());
        E e = container.resolve(E.class);
        assertThat(e, notNullValue());
    }

    @Test(expected = NonAccessibleFieldException.class)
    @Ignore("JVM is not throwing IllegalAccessException")
    public void testNonAccessibleFieldThrowStrategy() throws Exception {
        Configuration configuration = new Configuration.Builder()
                .nonAccessibleFieldStrategy(Configuration.NonAccessibleFieldStrategy.THROW)
                .build();
        IoC container = IoC.create(configuration, createDependencyResolver());
        container.resolve(E.class);
        Assert.fail();
    }
}
