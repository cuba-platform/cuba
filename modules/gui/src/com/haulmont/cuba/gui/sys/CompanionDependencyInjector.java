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

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.client.ClientConfiguration;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @deprecated Companions supported only for old-fashioned AbstractWindow and AbstractFrame
 */
@Deprecated
public class CompanionDependencyInjector {

    private static final Logger log = LoggerFactory.getLogger(CompanionDependencyInjector.class);

    protected Object companion;
    protected LegacyFrame frameOwner;

    protected BeanLocator beanLocator;

    public CompanionDependencyInjector(LegacyFrame frameOwner, Object companion) {
        this.companion = companion;
        this.frameOwner = frameOwner;
    }

    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    public void inject() {
        Map<AnnotatedElement, Class> toInject = new HashMap<>();

        List<Class<?>> classes = ClassUtils.getAllSuperclasses(companion.getClass());
        classes.add(0, companion.getClass());
        Collections.reverse(classes);

        for (Field field : getAllFields(classes)) {
            Class aClass = injectionAnnotation(field);
            if (aClass != null) {
                toInject.put(field, aClass);
            }
        }
        for (Method method : companion.getClass().getMethods()) {
            Class aClass = injectionAnnotation(method);
            if (aClass != null) {
                toInject.put(method, aClass);
            }
        }

        for (Map.Entry<AnnotatedElement, Class> entry : toInject.entrySet()) {
            doInjection(entry.getKey(), entry.getValue());
        }
    }

    private List<Field> getAllFields(List<Class<?>> classes) {
        List<Field> list = new ArrayList<>();

        for (Class c : classes) {
            if (c != Object.class) {
                for (Field field : c.getDeclaredFields()) {
                    int idx = indexOfFieldWithSameName(list, field);
                    if (idx > -1)
                        list.set(idx, field);
                    else
                        list.add(field);
                }
            }
        }
        return list;
    }

    private int indexOfFieldWithSameName(List<Field> fields, Field field) {
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            if (f.getName().equals(field.getName()))
                return i;
        }
        return -1;
    }

    private Class injectionAnnotation(AnnotatedElement element) {
        if (element.isAnnotationPresent(Named.class))
            return Named.class;
        else if (element.isAnnotationPresent(Resource.class))
            return Resource.class;
        else if (element.isAnnotationPresent(Inject.class))
            return Inject.class;
        else
            return null;
    }

    private void doInjection(AnnotatedElement element, Class annotationClass) {
        Class<?> type;
        String name = null;
        if (annotationClass == Named.class)
            name = element.getAnnotation(Named.class).value();
        else if (annotationClass == Resource.class)
            name = element.getAnnotation(Resource.class).name();

        if (element instanceof Field) {
            type = ((Field) element).getType();
            if (StringUtils.isEmpty(name))
                name = ((Field) element).getName();
        } else if (element instanceof Method) {
            Class<?>[] types = ((Method) element).getParameterTypes();
            if (types.length != 1)
                throw new IllegalStateException("Can inject to methods with one parameter only");
            type = types[0];
            if (StringUtils.isEmpty(name)) {
                if (((Method) element).getName().startsWith("set"))
                    name = StringUtils.uncapitalize(((Method) element).getName().substring(3));
                else
                    name = ((Method) element).getName();
            }
        } else
            throw new IllegalStateException("Can inject to fields and setter methods only");

        Object instance = getInjectedInstance(type, name, element);
        if (instance == null) {
            log.warn("Unable to find an instance of type " + type + " named " + name);
        } else {
            assignValue(element, instance);
        }
    }

    private Object getInjectedInstance(Class<?> type, String name, AnnotatedElement element) {
        if (Component.class.isAssignableFrom(type)) {
            // Injecting a UI component
            return frameOwner.getComponent(name);

        } else if (Datasource.class.isAssignableFrom(type)) {
            // Injecting a datasource
            return frameOwner.getDsContext().get(name);

        } else if (DsContext.class.isAssignableFrom(type)) {
            // Injecting the DsContext
            return frameOwner.getDsContext();

        } else if (DataSupplier.class.isAssignableFrom(type)) {
            // Injecting the DataSupplier
            return frameOwner.getDsContext().getDataSupplier();

        } else if (FrameContext.class.isAssignableFrom(type)) {
            // Injecting the FrameContext
            return frameOwner.getContext();

        } else if (Action.class.isAssignableFrom(type)) {
            // Injecting an action
            return ComponentsHelper.findAction(name, frameOwner.getWrappedFrame());

        } else if (ExportDisplay.class.isAssignableFrom(type)) {
            // Injecting an ExportDisplay
            ExportDisplay exportDisplay = beanLocator.get(ExportDisplay.NAME);
            exportDisplay.setFrame(frameOwner.getWrappedFrame());
            return exportDisplay;

        } else if (ThemeConstants.class.isAssignableFrom(type)) {
            // Injecting a Theme
            ThemeConstantsManager themeManager = beanLocator.get(ThemeConstantsManager.NAME);
            return themeManager.getConstants();

        } else if (Logger.class == type && element instanceof Field) {
            return LoggerFactory.getLogger(((Field) element).getDeclaringClass());
        } else if (Config.class.isAssignableFrom(type)) {
            ClientConfiguration configuration = beanLocator.get(Configuration.NAME);
            //noinspection unchecked
            return configuration.getConfigCached((Class<? extends Config>) type);

        } else {
            Object instance;
            // Try to find a Spring bean
            Map<String, ?> beans = beanLocator.getAll(type);
            if (!beans.isEmpty()) {
                instance = beans.get(name);
                // If a bean with required name found, return it. Otherwise return first found.
                if (instance != null)
                    return instance;
                else
                    return beans.values().iterator().next();
            }
            // There are no Spring beans of required type - the last option is Frame
            if (type.isAssignableFrom(FrameOwner.class)) {
                return frameOwner;
            }
            return null;
        }
    }

    private void assignValue(AnnotatedElement element, Object value) {
        if (element instanceof Field) {
            ((Field) element).setAccessible(true);
            try {
                ((Field) element).set(companion, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("CDI - Unable to assign value to field " + ((Field) element).getName(), e);
            }
        } else {
            Object[] params = new Object[1];
            params[0] = value;
            ((Method) element).setAccessible(true);
            try {
                ((Method) element).invoke(companion, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("CDI - Unable to assign value through setter "
                        + ((Method) element).getName(), e);
            }
        }
    }
}