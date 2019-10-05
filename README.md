Simple IoC Container [![Build Status](https://travis-ci.org/jruesga/simple-ioc-container.svg?branch=master)](https://travis-ci.org/jruesga/simple-ioc-container/branches) [![Apache 2.0](https://img.shields.io/github/license/jruesga/simple-ioc-container.svg)](http://www.apache.org/licenses/LICENSE-2.0)
====================

### Description

A simple Inversion of Code container library.

### Building

This library used Gradle as build system. It can be build by typing the following command:

```
./gradlew clean test javadoc assemble
```

### Usage

An IoC reference can be obtained by calling one of IoC#create(DependencyResolver...) or IoC#create(Configuration, DependencyResolver...) methods.

Several DependencyResolver implementations can be provided in order to resolve dependencies. A Configuration class can also be provided to change the behaviour of IoC#resolve(Class) method.

An example of a basic usage of this class is described below:
```
Configuration configuration = new Configuration.Builder()
    .circularDependencyStrategy(CircularDependencyStrategy.THROW)
    .build();
BasicDependencyResolver resolver =
            new BasicDependencyResolver.Builder()
                    .register(Service.class, ServiceImpl.class)
                    .build();
IoC container = IoC.create(configuration, resolver);
Service service = container.resolve(Service.class);
service.callMethod();
```

This container uses the [JSR-330](https://jcp.org/en/jsr/detail?id=330 "JSR 330") standard to determine the fields that need to be auto-injected, by detecting [javax.inject.Inject](https://docs.oracle.com/javaee/6/api/javax/inject/Inject.html "javax.inject.Inject") annotation.

```
public class Service1 {
    @Inject
    private Service2 service2;
}
public class Service2 {
}
```

### Licenses

This source was released under the terms of [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) license.
