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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.haulmont.cuba.client.ClientConfiguration;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.events.sys.UiEventListenerMethodAdapter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Wires {@link Inject}, {@link Named}, {@link WindowParam} fields/setters and {@link EventListener} methods.
 */
@org.springframework.stereotype.Component(ControllerDependencyInjector.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllerDependencyInjector {

    public static final String NAME = "cuba_ControllerDependencyInjector";

    protected final LoadingCache<Class<?>, List<Method>> eventListenerMethodsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, List<Method>>() {
                        @Override
                        public List<Method> load(@Nonnull Class<?> concreteClass) {
                            return getAnnotatedListenerMethodsNotCached(concreteClass);
                        }
                    });

    protected Frame frame;
    protected Map<String, Object> params;

    protected BeanLocator beanLocator;

    public ControllerDependencyInjector(Frame frame, Map<String, Object> params) {
        this.frame = frame;
        this.params = params;
    }

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    public void inject() {
        Map<AnnotatedElement, Class> toInject = Collections.emptyMap(); // lazily initialized

        @SuppressWarnings("unchecked")
        List<Class<?>> classes = ClassUtils.getAllSuperclasses(frame.getClass());
        classes.add(0, frame.getClass());
        Collections.reverse(classes);

        for (Field field : getAllFields(classes)) {
            Class aClass = injectionAnnotation(field);
            if (aClass != null) {
                if (toInject.isEmpty()) {
                    toInject = new HashMap<>();
                }
                toInject.put(field, aClass);
            }
        }
        for (Method method : frame.getClass().getMethods()) {
            Class aClass = injectionAnnotation(method);
            if (aClass != null) {
                if (toInject.isEmpty()) {
                    toInject = new HashMap<>();
                }
                toInject.put(method, aClass);
            }
        }

        for (Map.Entry<AnnotatedElement, Class> entry : toInject.entrySet()) {
            doInjection(entry.getKey(), entry.getValue());
        }

        injectEventListeners(frame);
    }

    protected void injectEventListeners(Frame frame) {
        Class<? extends Frame> clazz = frame.getClass();

        List<Method> eventListenerMethods = getAnnotatedListenerMethods(clazz);

        if (!eventListenerMethods.isEmpty()) {
            Events events = beanLocator.get(Events.NAME);

            List<ApplicationListener> listeners = eventListenerMethods.stream()
                    .map(m -> new UiEventListenerMethodAdapter(frame, clazz, m, events))
                    .collect(Collectors.toList());

            ((AbstractFrame) frame).setUiEventListeners(listeners);
        }
    }

    protected List<Method> getAnnotatedListenerMethods(Class<?> clazz) {
        if (clazz == AbstractWindow.class
                || clazz == AbstractEditor.class
                || clazz == AbstractLookup.class
                || clazz == AbstractFrame.class) {
            return Collections.emptyList();
        }

        return eventListenerMethodsCache.getUnchecked(clazz);
    }

    protected static List<Method> getAnnotatedListenerMethodsNotCached(Class<?> clazz) {
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);

        List<Method> eventListenerMethods = Arrays.stream(methods)
                .filter(m -> m.getAnnotation(EventListener.class) != null)
                .collect(Collectors.toList());

        if (eventListenerMethods.isEmpty()) {
            return Collections.emptyList();
        }

        return ImmutableList.copyOf(eventListenerMethods);
    }

    protected List<Field> getAllFields(List<Class<?>> classes) {
        List<Field> list = new ArrayList<>();

        for (Class c : classes) {
            if (c != Object.class) {
                Collections.addAll(list, c.getDeclaredFields());
            }
        }
        return list;
    }

    protected Class injectionAnnotation(AnnotatedElement element) {
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

    protected void doInjection(AnnotatedElement element, Class annotationClass) {
        Class<?> type;
        String name = null;
        if (annotationClass == Named.class) {
            name = element.getAnnotation(Named.class).value();
        } else if (annotationClass == Resource.class) {
            name = element.getAnnotation(Resource.class).name();
        } else if (annotationClass == WindowParam.class) {
            name = element.getAnnotation(WindowParam.class).name();
        }

        boolean required = true;
        if (element.isAnnotationPresent(WindowParam.class))
            required = element.getAnnotation(WindowParam.class).required();

        if (element instanceof Field) {
            type = ((Field) element).getType();
            if (StringUtils.isEmpty(name)) {
                name = ((Field) element).getName();
            }
        } else if (element instanceof Method) {
            Class<?>[] types = ((Method) element).getParameterTypes();
            if (types.length != 1)
                throw new IllegalStateException("Can inject to methods with one parameter only");
            type = types[0];
            if (StringUtils.isEmpty(name)) {
                if (((Method) element).getName().startsWith("set")) {
                    name = StringUtils.uncapitalize(((Method) element).getName().substring(3));
                } else {
                    name = ((Method) element).getName();
                }
            }
        } else {
            throw new IllegalStateException("Can inject to fields and setter methods only");
        }

        Object instance = getInjectedInstance(type, name, annotationClass, element);

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

            Logger log = LoggerFactory.getLogger(ControllerDependencyInjector.class);
            log.warn(msg);
        }
    }

    protected Object getInjectedInstance(Class<?> type, String name, Class annotationClass, AnnotatedElement element) {
        if (annotationClass == WindowParam.class) {
            //Injecting a parameter
            return params.get(name);

        } else if (Component.class.isAssignableFrom(type)) {
            // Injecting a UI component
            return frame.getComponent(name);

        } else if (Datasource.class.isAssignableFrom(type)) {
            // Injecting a datasource
            return ((LegacyFrame) frame).getDsContext().get(name);

        } else if (DsContext.class.isAssignableFrom(type)) {
            // Injecting the DsContext
            return ((LegacyFrame) frame).getDsContext();

        } else if (DataSupplier.class.isAssignableFrom(type)) {
            // Injecting the DataSupplier
            return ((LegacyFrame) frame).getDsContext().getDataSupplier();

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
            ThemeConstantsManager themeManager = beanLocator.get(ThemeConstantsManager.NAME);
            return themeManager.getConstants();

        } else if (Config.class.isAssignableFrom(type)) {
            ClientConfiguration configuration = beanLocator.get(Configuration.NAME);
            //noinspection unchecked
            return configuration.getConfigCached((Class<? extends Config>) type);

        } else if (Logger.class == type && element instanceof Field) {
            return LoggerFactory.getLogger(((Field) element).getDeclaringClass());
        } else {
            Object instance;
            // Try to find a Spring bean
            Map<String, ?> beans = beanLocator.getAll(type);
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
        return null;
    }

    protected void assignValue(AnnotatedElement element, Object value) {
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
                throw new RuntimeException("CDI - Unable to assign value through setter "
                        + ((Method) element).getName(), e);
            }
        }
    }
}