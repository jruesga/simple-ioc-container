package com.ruesga.siocc.resolvers;

public interface DependencyResolver {
    <T> T resolve(Class<T> type);
}
