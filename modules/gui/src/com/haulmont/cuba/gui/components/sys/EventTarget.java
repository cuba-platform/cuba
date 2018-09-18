package com.haulmont.cuba.gui.components.sys;

import com.haulmont.bali.events.Subscription;

import java.util.function.Consumer;

/**
 * Object that supports generics event subscription mechanism.
 */
public interface EventTarget {
    <E> Subscription addListener(Class<E> eventType, Consumer<E> listener);

    <E> boolean removeListener(Class<E> eventType, Consumer<E> listener);
}