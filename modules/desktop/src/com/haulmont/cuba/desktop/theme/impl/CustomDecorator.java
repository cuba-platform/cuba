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
 *
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.desktop.theme.ComponentDecorator;

import java.util.Set;

/**
 */
public class CustomDecorator implements ComponentDecorator {

    private String className;

    public CustomDecorator(String className) {
        this.className = className;
    }

    @Override
    public void decorate(Object component, Set<String> state) {
        Class decoratorClass = AppBeans.get(Scripting.class).loadClassNN(className);
        try {
            ComponentDecorator delegate = (ComponentDecorator) decoratorClass.newInstance();
            delegate.decorate(component, state);
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
