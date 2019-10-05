package com.ruesga.siocc.showcase.components;

import javax.inject.Inject;

public class Service2Impl implements Service2 {
    @Inject
    private Class1 c1;

    public void print(String msg) {
        System.out.println("Service2Impl => " + msg);
    }
    public void doSomething() {
        c1.doSomething();
    }
}
