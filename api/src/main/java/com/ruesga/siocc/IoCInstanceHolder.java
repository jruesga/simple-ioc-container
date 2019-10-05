package com.ruesga.siocc;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link IoC} instance holder to provided easy access to containers singleton instances.
 * <br>
 * <pre>
 *     IoCInstanceHolder.register("my-tag", IoC.create(...));
 *     ...
 *     IoC container = IoCInstanceHolder.of("my-tag")
 *     container.resolve(...);
 * </pre>
 */
public class IoCInstanceHolder {
    private static Map<String, IoC> containers = new HashMap<>();

    /**
     * Registers a container and associate it to a tag.
     *
     * @param tag the associated tag.
     * @param container the {@link IoC} container.
     */
    public static void register(String tag, IoC container) {
        containers.put(tag, container);
    }

    /**
     * Retrieves a registered container.
     *
     * @param tag the associated tag.
     * @return the {@link IoC} reference or <code>null</code> if there is not container associated
     *         to the tag.
     */
    public static IoC of(String tag) {
        return containers.get(tag);
    }
}
