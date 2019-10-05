package com.ruesga.siocc.showcase;

import com.ruesga.siocc.Configuration;
import com.ruesga.siocc.IoC;
import com.ruesga.siocc.resolvers.AnnotationDependencyResolver;
import com.ruesga.siocc.resolvers.BasicDependencyResolver;
import com.ruesga.siocc.showcase.components.Service1;
import com.ruesga.siocc.showcase.components.Service2;
import com.ruesga.siocc.showcase.components.Service2Impl;

public class Showcase {
    public static void main(String[] args) throws Exception {
        // Create a configuration that throws exceptions when a circular dependency is detected
        Configuration configuration = new Configuration.Builder()
                .circularDependencyStrategy(Configuration.CircularDependencyStrategy.THROW)
                .build();

        // Create 2 dependency resolvers (a basic one and annotations based one)
        BasicDependencyResolver basicResolver =
                new BasicDependencyResolver.Builder()
                        .register(Service2.class, Service2Impl.class)
                        .build();
        AnnotationDependencyResolver annotationResolver =
                new AnnotationDependencyResolver.Builder()
                        .scan("com.ruesga.siocc.showcase.components")
                        .build();

        // Create the IoC contatiner
        IoC container = IoC.create(configuration, basicResolver, annotationResolver);


        // Resolve classes
        Service1 service1 = container.resolve(Service1.class);
        service1.print("hello!"); // Service1Impl => hello! from AnnotationDependencyResolver
        Service2 service2 = container.resolve(Service2.class);
        service2.print("hello!"); // Service2Impl => hello! from BasicDependencyResolver
    }
}
