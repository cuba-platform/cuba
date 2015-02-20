/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.client.ClientConfiguration;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CompanionDependencyInjector {

    private static final Log log = LogFactory.getLog(CompanionDependencyInjector.class);

    protected Object companion;
    protected IFrame frame;

    public CompanionDependencyInjector(IFrame frame, Object companion) {
        this.companion = companion;
        this.frame = frame;
    }

    public void inject() {
        Map<AnnotatedElement, Class> toInject = new HashMap<>();

        @SuppressWarnings("unchecked")
        List<Class> classes = ClassUtils.getAllSuperclasses(companion.getClass());
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

    private List<Field> getAllFields(List<Class> classes) {
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

        Object instance = getInjectedInstance(type, name);
        if (instance == null) {
            log.warn("Unable to find an instance of type " + type + " named " + name);
        } else {
            assignValue(element, instance);
        }
    }

    private Object getInjectedInstance(Class<?> type, String name) {
        if (Component.class.isAssignableFrom(type)) {
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
                if (instance != null)
                    return instance;
                else
                    return beans.values().iterator().next();
            }
            // There are no Spring beans of required type - the last option is Frame
            if (type.isAssignableFrom(IFrame.class)) {
                return frame;
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
                throw new RuntimeException(e);
            }
        } else {
            Object[] params = new Object[1];
            params[0] = value;
            ((Method) element).setAccessible(true);
            try {
                ((Method) element).invoke(companion, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}