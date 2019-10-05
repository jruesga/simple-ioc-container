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


### Licenses

This source was released under the terms of [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) license.
