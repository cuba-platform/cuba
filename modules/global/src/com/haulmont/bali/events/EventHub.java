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

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * EventHub class implementing the event listening model with concrete event classes.
 */
@NotThreadSafe
public class EventHub {
    protected static final int EVENTS_MAP_EXPECTED_MAX_SIZE = 4;

    protected LinkedHashSet<Tag<?>> events = null;

    /**
     * Add an event listener for events with type T.
     *
     * @param eventType event class
     * @param listener  listener
     * @param <T>       type of event
     * @return registration object
     */
    public <T> Subscription subscribe(Class<T> eventType, Consumer<T> listener) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (events == null) {
            events = new LinkedHashSet<>(EVENTS_MAP_EXPECTED_MAX_SIZE);
        }

        Tag<T> tag = new Tag<>(eventType, listener);
        events.add(tag);

        return new SubscriptionImpl<>(this, eventType, listener);
    }

    /**
     * Remove an event listener for events with type T.
     *
     * @param eventType event class
     * @param listener  listener
     * @param <T>       type of event
     * @return true if listener has been removed
     */
    public <T> boolean unsubscribe(Class<T> eventType, Consumer<T> listener) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (events != null) {
            Iterator<Tag<?>> i = events.iterator();
            while (i.hasNext()) {
                Tag t = i.next();
                if (t.matches(eventType, listener)) {
                    i.remove();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if there are listeners for event type T.
     *
     * @param eventType event class
     * @param <T>       type of event
     * @return true if there are one or more listeners for type T
     */
    public <T> boolean hasSubscriptions(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }

        if (events == null) {
            return false;
        }

        for (Tag<?> event : events) {
            if (event.isType(eventType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fire listeners for event type T.
     *
     * @param eventType event class
     * @param event     event object
     * @param <T>       type of event
     */
    public <T> void publish(Class<T> eventType, T event) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }

        if (events != null) {
            Tag<?>[] tags = events.toArray(new Tag[0]);

            for (Tag<?> tag : tags) {
                if (tag.isType(eventType)) {
                    @SuppressWarnings("unchecked")
                    Consumer<T> listener = (Consumer<T>) tag.getListener();
                    listener.accept(event);
                }
            }
        }
    }

    protected static class SubscriptionImpl<T> implements Subscription {
        private final EventHub publisher;
        private final Class<T> eventClass;
        private final Consumer<T> listener;

        public SubscriptionImpl(EventHub publisher, Class<T> eventClass, Consumer<T> listener) {
            this.publisher = publisher;
            this.eventClass = eventClass;
            this.listener = listener;
        }

        @Override
        public void remove() {
            publisher.unsubscribe(eventClass, listener);
        }
    }

    protected static class Tag<T> {
        protected final Class<T> eventClass;
        protected final Consumer<T> listener;

        public Tag(Class<T> eventClass, Consumer<T> listener) {
            this.eventClass = eventClass;
            this.listener = listener;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tag<?> tag = (Tag<?>) o;
            return Objects.equals(eventClass, tag.eventClass) &&
                    Objects.equals(listener, tag.listener);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventClass, listener);
        }

        @Override
        public String toString() {
            return "Tag{" +
                    "eventClass=" + eventClass +
                    ", listener=" + listener +
                    '}';
        }

        public boolean matches(Class eventClass, Consumer listener) {
            return Objects.equals(eventClass, this.eventClass) &&
                    Objects.equals(listener, this.listener);
        }

        public Consumer<T> getListener() {
            return listener;
        }

        public boolean isType(Class<?> eventClass) {
            return this.eventClass == eventClass;
        }
    }
}