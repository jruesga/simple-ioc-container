package com.ruesga.siocc.resolvers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to declare a class as injectable <b><i>IoC</i></b> component. {@link IoCDependency}
 * annotated classes are auto-registered and can be resolved
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IoCDependency {
}
