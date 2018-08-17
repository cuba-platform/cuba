package com.haulmont.cuba.gui.sys;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.screen.Subscribe;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;

// todo Unit tests
@Component("cuba_ScreenReflectionInspector")
public class ScreenReflectionInspector {

    protected final LoadingCache<Class<?>, List<Method>> eventListenerMethodsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, List<Method>>() {
                        @Override
                        public List<Method> load(@Nonnull Class<?> concreteClass) {
                            return getAnnotatedListenerMethodsNotCached(concreteClass);
                        }
                    });

    protected final LoadingCache<Class<?>, List<Method>> subscribeMethodsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, List<Method>>() {
                        @Override
                        public List<Method> load(@Nonnull Class<?> concreteClass) {
                            return getAnnotatedSubscribeMethodsNotCached(concreteClass);
                        }
                    });

    public List<Method> getAnnotatedSubscribeMethods(Class<?> clazz) {
        return subscribeMethodsCache.getUnchecked(clazz);
    }

    public List<Method> getAnnotatedListenerMethods(Class<?> clazz) {
        if (clazz == AbstractWindow.class
                || clazz == AbstractEditor.class
                || clazz == AbstractLookup.class
                || clazz == AbstractFrame.class) {
            return Collections.emptyList();
        }

        return eventListenerMethodsCache.getUnchecked(clazz);
    }

    protected List<Method> getAnnotatedListenerMethodsNotCached(Class<?> clazz) {
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);

        List<Method> eventListenerMethods = Arrays.stream(methods)
                .filter(m -> m.getAnnotation(EventListener.class) != null)
                .collect(Collectors.toList());

        if (eventListenerMethods.isEmpty()) {
            return Collections.emptyList();
        }

        for (Method method : eventListenerMethods) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
        }

        return ImmutableList.copyOf(eventListenerMethods);
    }

    protected List<Method> getAnnotatedSubscribeMethodsNotCached(Class<?> clazz) {
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);

        List<Method> subscribeMethods = Arrays.stream(methods)
                .filter(m -> m.getAnnotation(Subscribe.class) != null)
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> EventObject.class.isAssignableFrom(m.getParameterTypes()[0]))
                .collect(Collectors.toList());

        if (subscribeMethods.isEmpty()) {
            return Collections.emptyList();
        }

        for (Method method : subscribeMethods) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
        }

        return ImmutableList.copyOf(subscribeMethods);
    }
}