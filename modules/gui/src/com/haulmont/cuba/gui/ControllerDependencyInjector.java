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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.client.ClientConfiguration;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.*;
import java.util.*;

/**
 */
public class ControllerDependencyInjector {

    private Frame frame;
    private Map<String,Object> params;

    private Logger log = LoggerFactory.getLogger(getClass());

    public ControllerDependencyInjector(Frame frame, Map<String,Object> params) {
        this.frame = frame;
        this.params = params;
    }

    public void inject() {
        Map<AnnotatedElement, Class> toInject = new HashMap<>();

        List<Class> classes = ClassUtils.getAllSuperclasses(frame.getClass());
        classes.add(0, frame.getClass());
        Collections.reverse(classes);

        for (Field field : getAllFields(classes)) {
            Class aClass = injectionAnnotation(field);
            if (aClass != null) {
                toInject.put(field, aClass);
            }
        }
        for (Method method : frame.getClass().getMethods()) {
            Class aClass = injectionAnnotation(method);
            if (aClass != null) {
                toInject.put(method, aClass);
            }
        }

        for (Map.Entry<AnnotatedElement, Class> entry : toInject.entrySet()) {
            doInjection(entry.getKey(), entry.getValue());
        }
    }

    private List<Field> getAllFields(List<Class> classes) {
        List<Field> list = new ArrayList<>();

        for (Class c : classes) {
            if (c != Object.class) {
                Collections.addAll(list, c.getDeclaredFields());
            }
        }
        return list;
    }

    private Class injectionAnnotation(AnnotatedElement element) {
        if (element.isAnnotationPresent(Named.class))
            return Named.class;
        else if (element.isAnnotationPresent(Resource.class))
            return Resource.class;
        else if (element.isAnnotationPresent(Inject.class))
            return Inject.class;
        else if (element.isAnnotationPresent(WindowParam.class))
            return WindowParam.class;
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
        else if (annotationClass == WindowParam.class)
            name = element.getAnnotation(WindowParam.class).name();

        boolean required = true;
        if (element.isAnnotationPresent(WindowParam.class))
            required = element.getAnnotation(WindowParam.class).required();

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
        } else {
            throw new IllegalStateException("Can inject to fields and setter methods only");
        }

        Object instance = getInjectedInstance(type, name, annotationClass);

        if (instance != null) {
            assignValue(element, instance);
        } else if (required) {
            Class<?> declaringClass = ((Member) element).getDeclaringClass();
            Class<? extends Frame> frameClass = frame.getClass();

            String msg;
            if (frameClass == declaringClass) {
                msg = String.format(
                        "CDI - Unable to find an instance of type '%s' named '%s' for instance of '%s'",
                        type, name, frameClass.getCanonicalName());
            } else {
                msg = String.format(
                        "CDI - Unable to find an instance of type '%s' named '%s' declared in '%s' for instance of '%s'",
                        type, name, declaringClass.getCanonicalName(), frameClass.getCanonicalName());
            }

            log.warn(msg);
        }
    }

    private Object getInjectedInstance(Class<?> type, String name, Class annotationClass) {
        if (annotationClass == WindowParam.class) {
            //Injecting a parameter
            return params.get(name);

        } else if (Component.class.isAssignableFrom(type)) {
            // Injecting a UI component
            return frame.getComponent(name);

        } else if (Datasource.class.isAssignableFrom(type)) {
            // Injecting a datasource
            return frame.getDsContext().get(name);

        } else if (DsContext.class.isAssignableFrom(type)) {
            // Injecting the DsContext
            return frame.getDsContext();

        } else if (DataSupplier.class.isAssignableFrom(type)) {
            // Injecting the DataSupplier
            return frame.getDsContext().getDataSupplier();

        } else if (FrameContext.class.isAssignableFrom(type)) {
            // Injecting the FrameContext
            return frame.getContext();

        } else if (Action.class.isAssignableFrom(type)) {
            // Injecting an action
            return ComponentsHelper.findAction(name, frame);

        } else if (ExportDisplay.class.isAssignableFrom(type)) {
            // Injecting an ExportDisplay
            return AppConfig.createExportDisplay(frame);

        } else if (ThemeConstants.class.isAssignableFrom(type)) {
            // Injecting a Theme
            ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
            return themeManager.getConstants();

        } else if (Config.class.isAssignableFrom(type)) {
            //noinspection unchecked
            ClientConfiguration configuration = AppBeans.get(Configuration.NAME);
            return configuration.getConfigCached((Class<? extends Config>) type);

        } else {
            Object instance;
            // Try to find a Spring bean
            Map<String, ?> beans = AppContext.getApplicationContext().getBeansOfType(type, true, true);
            if (!beans.isEmpty()) {
                instance = beans.get(name);
                // If a bean with required name found, return it. Otherwise return first found.
                if (instance != null) {
                    return instance;
                } else {
                    return beans.values().iterator().next();
                }
            }
            // There are no Spring beans of required type - the last option is Companion
            if (frame instanceof AbstractFrame) {
                instance = ((AbstractFrame) frame).getCompanion();
                if (instance != null && type.isAssignableFrom(instance.getClass())) {
                    return instance;
                }
            }
        }
        return  null;
    }

    private void assignValue(AnnotatedElement element, Object value) {
        if (element instanceof Field) {
            ((Field) element).setAccessible(true);
            try {
                ((Field) element).set(frame, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("CDI - Unable to assign value to field " + ((Field) element).getName(), e);
            }
        } else {
            Object[] params = new Object[1];
            params[0] = value;
            ((Method) element).setAccessible(true);
            try {
                ((Method) element).invoke(frame, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("CDI - Unable to assign value through setter " + ((Field) element).getName(),e);
            }
        }
    }
}