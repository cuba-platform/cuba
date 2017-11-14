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

package com.haulmont.cuba.web.sys;

import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class VaadinSessionScope implements Scope {

    public static final String NAME = "vaadin";

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (VaadinSession.getCurrent() == null || !VaadinSession.getCurrent().hasLock()) {
            throw new IllegalStateException("Unable to use VaadinSessionScope from non-Vaadin thread");
        }

        Object object = VaadinSession.getCurrent().getAttribute(name);
        if (object == null) {
            object = objectFactory.getObject();
            VaadinSession.getCurrent().setAttribute(name, object);
        }
        return object;
    }

    @Override
    public Object remove(String name) {
        if (VaadinSession.getCurrent() == null || !VaadinSession.getCurrent().hasLock()) {
            throw new IllegalStateException("Unable to use VaadinSessionScope from non-Vaadin thread");
        }

        Object bean = VaadinSession.getCurrent().getAttribute(name);
        VaadinSession.getCurrent().setAttribute(name, null);
        return bean;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        if (VaadinSession.getCurrent() == null || !VaadinSession.getCurrent().hasLock()) {
            throw new IllegalStateException("Unable to use VaadinSessionScope from non-Vaadin thread");
        }

        return VaadinSession.getCurrent().getSession().getId();
    }
}