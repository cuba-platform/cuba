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
package com.haulmont.cuba.gui.sys;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.*;
import java.util.*;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@Component("cuba_UiControllerReflectionInspector")
public class UiControllerReflectionInspector {

    protected final LoadingCache<Class<?>, List<InjectElement>> injectValueElementsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, List<InjectElement>>() {
                        @Override
                        public List<InjectElement> load(@Nonnull Class<?> concreteClass) {
                            return getAnnotatedInjectElementsNotCached(concreteClass);
                        }
                    });

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

    public List<InjectElement> getAnnotatedInjectElements(Class<?> clazz) {
        return injectValueElementsCache.getUnchecked(clazz);
    }

    protected List<InjectElement> getAnnotatedInjectElementsNotCached(Class<?> clazz) {
        Map<AnnotatedElement, Class> toInject = Collections.emptyMap(); // lazily initialized

        @SuppressWarnings("unchecked")
        List<Class<?>> classes = ClassUtils.getAllSuperclasses(clazz);
        classes.add(0, clazz);
        Collections.reverse(classes);

        for (Field field : getAllFields(classes)) {
            Class aClass = injectionAnnotation(field);
            if (aClass != null) {
                if (toInject.isEmpty()) {
                    toInject = new HashMap<>();
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                toInject.put(field, aClass);
            }
        }
        for (Method method : ReflectionUtils.getUniqueDeclaredMethods(clazz)) {
            Class aClass = injectionAnnotation(method);
            if (aClass != null) {
                if (toInject.isEmpty()) {
                    toInject = new HashMap<>();
                }
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                toInject.put(method, aClass);
            }
        }

        return toInject.entrySet().stream()
                .map(entry -> new InjectElement(entry.getKey(), entry.getValue()))
                .collect(ImmutableList.toImmutableList());
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
        if (element.isAnnotationPresent(Named.class)) {
            return Named.class;
        }

        if (element.isAnnotationPresent(Resource.class)) {
            return Resource.class;
        }

        if (element.isAnnotationPresent(Inject.class)) {
            return Inject.class;
        }

        if (element.isAnnotationPresent(WindowParam.class)) {
            return WindowParam.class;
        }

        return null;
    }

    protected List<Method> getAnnotatedListenerMethodsNotCached(Class<?> clazz) {
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);

        return Arrays.stream(methods)
                .filter(m -> findMergedAnnotation(m, EventListener.class) != null)
                .peek(m -> {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                })
                .collect(ImmutableList.toImmutableList());
    }

    protected List<Method> getAnnotatedSubscribeMethodsNotCached(Class<?> clazz) {
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);

        return Arrays.stream(methods)
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> EventObject.class.isAssignableFrom(m.getParameterTypes()[0]))
                .filter(m -> findMergedAnnotation(m, Subscribe.class) != null)
                .peek(m -> {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                })
                .sorted(this::compareSubscribeMethods)
                .collect(ImmutableList.toImmutableList());
    }

    protected int compareSubscribeMethods(Method m1, Method m2) {
        if (m1 == m2) {
            // fulfill comparator contract
            return 0;
        }

        Class<?> pt1 = m1.getParameterTypes()[0];
        Class<?> pt2 = m2.getParameterTypes()[0];

        if (pt1 != pt2) {
            // if type of event different - compare by class name
            return pt1.getCanonicalName().compareTo(pt2.getCanonicalName());
        }

        Order o1 = findMergedAnnotation(m1, Order.class);
        Order o2 = findMergedAnnotation(m2, Order.class);

        if (o1 != null && o2 != null) {
            return Integer.compare(o1.value(), o2.value());
        }

        if (o1 != null) {
            return -1;
        }

        if (o2 != null) {
            return 1;
        }

        Class<?> dc1 = getDeclaringClass(m1);
        Class<?> dc2 = getDeclaringClass(m2);

        if (dc1 == dc2) {
            // if declaring class is the same - compare by method name
            return m1.getName().compareTo(m2.getName());
        }

        // if there is no @Order - parent first

        if (dc1.isAssignableFrom(dc2)) {
            return -1;
        }

        if (dc2.isAssignableFrom(dc1)) {
            return 1;
        }

        // return 0 as fallback
        return 0;
    }

    protected Class<?> getDeclaringClass(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.getSuperclass() == Screen.class
            || declaringClass.getSuperclass() == ScreenFragment.class) {
            // speed up search of declaring class for simple cases
            return declaringClass;
        }

        Set<Method> overrideHierarchy = getOverrideHierarchy(method);
        return Iterables.getLast(overrideHierarchy).getDeclaringClass();
    }

    protected Set<Method> getOverrideHierarchy(Method method) {
        Set<Method> result = new LinkedHashSet<>();
        result.add(method);

        Class<?>[] parameterTypes = method.getParameterTypes();

        Class<?> declaringClass = method.getDeclaringClass();

        Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, ClassUtils.Interfaces.INCLUDE).iterator();
        //skip the declaring class :P
        hierarchy.next();
        hierarchyTraversal: while (hierarchy.hasNext()) {
            final Class<?> c = hierarchy.next();
            Method m;
            try {
                m = c.getDeclaredMethod(method.getName(), parameterTypes);
            } catch (NoSuchMethodException e) {
                m = null;
            }

            if (m == null) {
                continue;
            }
            if (Arrays.equals(m.getParameterTypes(), parameterTypes)) {
                // matches without generics
                result.add(m);
                continue;
            }
            // necessary to get arguments every time in the case that we are including interfaces
            Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());
            for (int i = 0; i < parameterTypes.length; i++) {
                Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
                Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
                if (!TypeUtils.equals(childType, parentType)) {
                    continue hierarchyTraversal;
                }
            }
            result.add(m);
        }
        return result;
    }

    public static class InjectElement {
        protected final AnnotatedElement element;
        protected final Class annotationClass;

        public InjectElement(AnnotatedElement element, Class annotationClass) {
            this.element = element;
            this.annotationClass = annotationClass;
        }

        public AnnotatedElement getElement() {
            return element;
        }

        public Class getAnnotationClass() {
            return annotationClass;
        }

        @Override
        public String toString() {
            return "InjectElement{" +
                    "element=" + element +
                    ", annotationClass=" + annotationClass +
                    '}';
        }
    }
}