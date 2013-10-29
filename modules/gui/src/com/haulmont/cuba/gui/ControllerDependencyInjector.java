/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.export.ExportDisplay;
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
public class ControllerDependencyInjector {

    private IFrame frame;
    private Map<String,Object> params;

    private Log log = LogFactory.getLog(getClass());

    public ControllerDependencyInjector(IFrame frame, Map<String,Object> params) {
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
        } else
            throw new IllegalStateException("Can inject to fields and setter methods only");

        Object instance = getInjectedInstance(type, name, annotationClass);
        if (required && instance == null)
            log.warn("CDI - Unable to find an instance of type " + type + " named " + name);
        else
            assignValue(element, instance);
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

        } else if (WindowContext.class.isAssignableFrom(type)) {
            // Injecting the WindowContext
            return frame.getContext();

        } else if (Action.class.isAssignableFrom(type)) {
            // Injecting an action
            return ComponentsHelper.findAction(name, frame);

        } else if (ExportDisplay.class.isAssignableFrom(type)) {
            // Injecting an ExportDisplay
            return AppConfig.createExportDisplay(frame);

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
            // There are no Spring beans of required type - the last option is Companion
            if (frame instanceof AbstractFrame) {
                instance = ((AbstractFrame) frame).getCompanion();
                if (instance != null && type.isAssignableFrom(instance.getClass()))
                    return instance;
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