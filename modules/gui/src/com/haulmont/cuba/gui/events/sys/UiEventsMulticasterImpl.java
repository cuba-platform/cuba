/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.events.sys;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.context.event.GenericApplicationListenerAdapter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NotThreadSafe
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(UiEventsMulticaster.NAME)
public class UiEventsMulticasterImpl implements UiEventsMulticaster {
    protected List<ApplicationListener<?>> listeners = new ArrayList<>();

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        Object source = event.getSource();
        Class<?> sourceType = (source != null ? source.getClass() : null);

        ResolvableType type = resolveDefaultEventType(event);
        for (ApplicationListener<?> listener : retrieveApplicationListeners(type, sourceType)) {
            invokeListener(listener, event);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void invokeListener(ApplicationListener listener, ApplicationEvent event) {
        try {
            listener.onApplicationEvent(event);
        } catch (ClassCastException ex) {
            String msg = ex.getMessage();
            if (msg == null || msg.startsWith(event.getClass().getName())) {
                // Possibly a lambda-defined listener which we could not resolve the generic event type for
                LoggerFactory.getLogger(UiEventsMulticaster.class)
                        .debug("Non-matching event type for listener: {}", listener, ex);
            } else {
                throw ex;
            }
        }
    }

    protected ResolvableType resolveDefaultEventType(ApplicationEvent event) {
        return ResolvableType.forInstance(event);
    }

    protected Collection<ApplicationListener<?>> retrieveApplicationListeners(ResolvableType eventType, @Nullable Class<?> sourceType) {
        return listeners.stream()
                .filter(listener -> supportsEvent(listener, eventType, sourceType))
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .collect(Collectors.toList());
    }

    protected boolean supportsEvent(ApplicationListener<?> listener, ResolvableType eventType, @Nullable Class<?> sourceType) {
        GenericApplicationListener smartListener = (listener instanceof GenericApplicationListener ?
                (GenericApplicationListener) listener : new GenericApplicationListenerAdapter(listener));
        return (smartListener.supportsEventType(eventType) && smartListener.supportsSourceType(sourceType));
    }
}