package com.ruesga.siocc.resolvers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(JUnit4.class)
public class AnnotationDependencyResolverTest {

    public interface Service {
    }

    @IoCDependency
    public static class ServiceImpl implements Service {
    }

    public interface NonService {
    }

    @Test
    public void testResolve() {
        AnnotationDependencyResolver resolver =
                new AnnotationDependencyResolver.Builder()
                        .scan("com.ruesga.siocc.resolvers")
                        .build();
        Service x = resolver.resolve(Service.class);
        assertThat(x, notNullValue());
        assertThat(x, instanceOf(ServiceImpl.class));

        x = resolver.resolve(ServiceImpl.class);
        assertThat(x, notNullValue());
        assertThat(x, instanceOf(ServiceImpl.class));

        NonService x1 = resolver.resolve(NonService.class);
        assertThat(x1, nullValue());
    }
}
