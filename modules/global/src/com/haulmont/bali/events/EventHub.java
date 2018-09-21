/*
 * Copyright (c) 2008-2018 Haulmont.
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

import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * EventHub class implementing the event listening model with concrete event classes.
 */
@NotThreadSafe
public class EventHub {
    protected static final int EVENTS_MAP_EXPECTED_MAX_SIZE = 4;

    protected static final Consumer[] EMPTY_LISTENERS_ARRAY = new Consumer[0];

    protected Map<Class<?>, Consumer[]> events;

    /**
     * Add an event listener for events with type E.
     *
     * @param eventType event class
     * @param listener  listener
     * @param <E>       type of event
     * @return subscription object
     */
    public <E> Subscription subscribe(Class<E> eventType, Consumer<E> listener) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (events == null) {
            events = new IdentityHashMap<>(EVENTS_MAP_EXPECTED_MAX_SIZE);
        }

        Consumer[] array = events.get(eventType);

        if (array == null || !ArrayUtils.contains(array, listener)) {
            int size = (array != null)
                    ? array.length
                    : 0;

            Consumer[] clone = newListenersArray(size + 1);
            clone[size] = listener;
            if (array != null) {
                System.arraycopy(array, 0, clone, 0, size);
            }
            events.put(eventType, clone);
        }

        return new SubscriptionImpl<>(this, eventType, listener);
    }

    /**
     * Remove an event listener for events with type E.
     *
     * @param eventType event class
     * @param listener  listener
     * @param <E>       type of event
     * @return true if listener has been removed
     */
    public <E> boolean unsubscribe(Class<E> eventType, Consumer<E> listener) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (events != null) {
            Consumer[] array = this.events.get(eventType);
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    if (listener.equals(array[i])) {
                        int size = array.length - 1;
                        if (size > 0) {
                            Consumer[] clone = newListenersArray(size);
                            System.arraycopy(array, 0, clone, 0, i);
                            System.arraycopy(array, i + 1, clone, i, size - i);
                            events.put(eventType, clone);
                        } else {
                            events.remove(eventType);
                            if (this.events.isEmpty()) {
                                this.events = null;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Remove all listeners with the specified event type.
     *
     * @param eventType event type
     */
    public void unsubscribe(Class<?> eventType) {
        if (events != null) {
            this.events.remove(eventType);
            if (this.events.isEmpty()) {
                this.events = null;
            }
        }
    }

    /**
     * Check if there are listeners for event type E.
     *
     * @param eventType event class
     * @param <E>       type of event
     * @return true if there are one or more listeners for type E
     */
    public <E> boolean hasSubscriptions(Class<E> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }

        if (events == null) {
            return false;
        }

        return events.get(eventType) != null;
    }

    /**
     * Fire listeners for event type E.
     *
     * @param eventType event class
     * @param event     event object
     * @param <E>       type of event
     */
    @SuppressWarnings("unchecked")
    public <E> void publish(Class<E> eventType, E event) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }

        if (events != null) {
            Consumer[] eventListeners = events.get(eventType);

            if (eventListeners != null) {
                for (Consumer listener : eventListeners) {
                    listener.accept(event);
                }

                TriggerOnce triggerOnce = eventType.getAnnotation(TriggerOnce.class);
                if (triggerOnce != null) {
                    unsubscribe(eventType);
                }
            }
        }
    }

    protected Consumer[] newListenersArray(int length) {
        return (0 < length)
                ? new Consumer[length]
                : EMPTY_LISTENERS_ARRAY;
    }

    protected static class SubscriptionImpl<E> implements Subscription {
        private final EventHub publisher;
        private final Class<E> eventClass;
        private final Consumer<E> listener;

        public SubscriptionImpl(EventHub publisher, Class<E> eventClass, Consumer<E> listener) {
            this.publisher = publisher;
            this.eventClass = eventClass;
            this.listener = listener;
        }

        @Override
        public void remove() {
            publisher.unsubscribe(eventClass, listener);
        }
    }
}