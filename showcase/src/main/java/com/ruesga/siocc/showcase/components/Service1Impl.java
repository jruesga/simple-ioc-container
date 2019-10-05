package com.ruesga.siocc.showcase.components;

import com.ruesga.siocc.resolvers.IoCDependency;

@IoCDependency
public class Service1Impl implements Service1 {
    public void print(String msg) {
        System.out.println("Service1Impl => " + msg);
    }
}
