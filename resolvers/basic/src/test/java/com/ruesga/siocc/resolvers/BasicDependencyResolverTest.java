package com.ruesga.siocc.resolvers;

import com.ruesga.siocc.resolvers.BasicDependencyResolver.Provider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(JUnit4.class)
public class BasicDependencyResolverTest {
    interface Service1 {
    }
    public static class Service1Impl implements Service1 {
    }

    static abstract class Service2 {
        private String msg;
        Service2(String msg) {
            this.msg = msg;
        }
        public String msg() {
            return msg;
        }
    }
    public static class Service2Impl extends Service2 {
        Service2Impl(String msg) {
            super(msg);
        }
    }
    public static class Service2ImplProvider implements Provider<Service2> {
        @Override
        public Service2 provide() {
            return new Service2Impl("hello!");
        }
    }

    interface Service3 {
    }
    public static class Service3Impl implements Service3 {
        private Service3Impl() {
        }
    }

    interface NonService {
    }


    @Test
    public void testResolve() throws Exception {
        BasicDependencyResolver resolver =
                new BasicDependencyResolver.Builder()
                        .register(Service1.class, Service1Impl.class)
                        .register(Service2.class, new Service2ImplProvider())
                        .register(Service3.class, Service3Impl.class)
                        .build();
        Service1 service1 = resolver.resolve(Service1.class);
        assertThat(service1, notNullValue());
        assertThat(service1, instanceOf(Service1Impl.class));

        Service2 service2 = resolver.resolve(Service2.class);
        assertThat(service2, notNullValue());
        assertThat(service2, instanceOf(Service2Impl.class));
        assertThat(service2.msg(), equalTo("hello!"));

        Service3 service3 = resolver.resolve(Service3.class);
        assertThat(service3, nullValue());

        NonService nonService = resolver.resolve(NonService.class);
        assertThat(nonService, nullValue());
    }

    @Test(expected = NonInstantiableClassException.class)
    public void testResolveNonInstantiable() throws Exception {
        new BasicDependencyResolver.Builder()
                        .register(Service2.class)
                        .build();
        Assert.fail();
    }
}
