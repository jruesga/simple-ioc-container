package com.ruesga.siocc;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(JUnit4.class)
public class IoCInstanceHolderTest {
    @BeforeClass
    public static void setUp() {
        IoCInstanceHolder.register("default", IoC.create());
    }

    @Test
    public void testOf() {
        IoC container = IoCInstanceHolder.of("default");
        assertThat(container, notNullValue());
    }

    @Test
    public void testOf_Null() {
        IoC container = IoCInstanceHolder.of("default2");
        assertThat(container, nullValue());
    }
}
