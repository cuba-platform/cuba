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

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.BeanLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component(BeanLocator.NAME)
public class BeanLocatorImpl implements BeanLocator, ApplicationContextAware {

    private static Map<Class, Optional<String>> names = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public <T> T get(Class<T> beanType) {
        Preconditions.checkNotNullArgument(beanType, "beanType is null");
        String name = findName(beanType);
        // If the name is found, look up the bean by name because it is much faster
        if (name == null)
            return applicationContext.getBean(beanType);
        else
            return applicationContext.getBean(name, beanType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String name) {
        Preconditions.checkNotNullArgument(name, "name is null");
        return (T) applicationContext.getBean(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String name, @Nullable Class<T> beanType) {
        Preconditions.checkNotNullArgument(name, "name is null");
        if (beanType != null) {
            return applicationContext.getBean(name, beanType);
        }
        return (T) applicationContext.getBean(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPrototype(String name, Object... args) {
        Preconditions.checkNotNullArgument(name, "name is null");
        return (T) applicationContext.getBean(name, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPrototype(Class<T> beanType, Object... args) {
        Preconditions.checkNotNullArgument(beanType, "beanType is null");
        String name = findName(beanType);
        // If the name is found, look up the bean by name
        if (name == null)
            return applicationContext.getBean(beanType, args);
        else
            return (T) applicationContext.getBean(name, args);
    }

    @Override
    public <T> Map<String, T> getAll(Class<T> beanType) {
        return applicationContext.getBeansOfType(beanType);
    }

    @Override
    public boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Nullable
    private <T> String findName(Class<T> beanType) {
        String name = null;
        Optional<String> optName = names.get(beanType);
        //noinspection OptionalAssignedToNull
        if (optName == null) {
            // Try to find a bean name defined in its NAME static field
            try {
                Field nameField = beanType.getField("NAME");
                name = (String) nameField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignore) {
            }
            names.put(beanType, Optional.ofNullable(name));
        } else {
            name = optName.orElse(null);
        }
        return name;
    }
}
