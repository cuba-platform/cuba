/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.bali.events;

import com.haulmont.bali.util.Preconditions;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Generic Event router with lazily initialized events map.
 */
@NotThreadSafe
public class EventRouter {
    private static final int EVENTS_MAP_EXPECTED_MAX_SIZE = 4;
    private static final int EVENTS_LIST_INITIAL_CAPACITY = 2;

    // Map with listener classes and listener lists
    // Lists are created on demand
    private Map<Class, List<Object>> events = null;

    public <L, E> void fireEvent(Class<L> listenerClass, BiConsumer<L, E> invoker, E event) {
        if (events != null) {
            @SuppressWarnings("unchecked")
            List<L> listeners = (List<L>) events.get(listenerClass);
            if (listeners != null) {
                for (Object listenerEntry : listeners.toArray()) {
                    @SuppressWarnings("unchecked")
                    L listener = (L) listenerEntry;
                    invoker.accept(listener, event);
                }
            }
        }
    }

    public <L> void addListener(Class<L> listenerClass, L listener) {
        Preconditions.checkNotNullArgument(listener, "listener cannot be null");

        if (events == null) {
            events = new IdentityHashMap<>(EVENTS_MAP_EXPECTED_MAX_SIZE);
        }

        List<Object> listeners = events.computeIfAbsent(listenerClass,
                clazz -> new ArrayList<>(EVENTS_LIST_INITIAL_CAPACITY));
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public <L> void addListener(Class<L> listenerClass, L listener, Runnable runIfFirstListenerWasAdded) {
        boolean isFirst = !hasListeners(listenerClass);

        addListener(listenerClass, listener);

        if (isFirst) {
            runIfFirstListenerWasAdded.run();
        }
    }

    public <L> void removeListener(Class<L> listenerClass, L listener) {
        Preconditions.checkNotNullArgument(listener, "listener cannot be null");

        if (events != null) {
            List<Object> listenersList = events.get(listenerClass);
            if (listenersList != null) {
                listenersList.remove(listener);
                if (listenersList.isEmpty()) {
                    events.remove(listenerClass);
                }
            }
        }
    }

    public <L> void removeListener(Class<L> listenerClass, L listener, Runnable runIfLastListenerWasRemoved) {
        boolean hadListeners = hasListeners(listenerClass);

        removeListener(listenerClass, listener);

        if (hadListeners && !hasListeners(listenerClass)) {
            runIfLastListenerWasRemoved.run();
        }
    }

    public <L> void removeListeners(Class<L> listenerClass) {
        if (events != null) {
            events.remove(listenerClass);
        }
    }

    public <L> boolean hasListeners(Class<L> listenerClass) {
        if (events != null) {
            List<Object> listeners = events.getOrDefault(listenerClass, Collections.emptyList());
            return !listeners.isEmpty();
        }

        return false;
    }
}