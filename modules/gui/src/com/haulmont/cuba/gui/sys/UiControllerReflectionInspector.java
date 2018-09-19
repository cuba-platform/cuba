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
import com.haulmont.cuba.gui.screen.Provide;
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
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.*;
import java.util.*;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@Component("cuba_UiControllerReflectionInspector")
public class UiControllerReflectionInspector {

    protected static final Method NO_METHOD_VALUE;
    static {
        try {
            NO_METHOD_VALUE = Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("It should never happen", e);
        }
    }

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

    protected final LoadingCache<Class<?>, List<AnnotatedMethod<Subscribe>>> subscribeMethodsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, List<AnnotatedMethod<Subscribe>>>() {
                        @Override
                        public List<AnnotatedMethod<Subscribe>> load(@Nonnull Class<?> concreteClass) {
                            return getAnnotatedSubscribeMethodsNotCached(concreteClass);
                        }
                    });

    protected final LoadingCache<Class<?>, List<AnnotatedMethod<Provide>>> provideMethodsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, List<AnnotatedMethod<Provide>>>() {
                        @Override
                        public List<AnnotatedMethod<Provide>> load(@Nonnull Class<?> concreteClass) {
                            return getAnnotatedProvideMethodsNotCached(concreteClass);
                        }
                    });

    protected final LoadingCache<MethodTag, Method> provideTargetMethodsCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<MethodTag, Method>() {
                        @Override
                        public Method load(@Nonnull MethodTag methodTag) {
                            return getProvideTargetMethodNotCached(methodTag);
                        }
                    });

    /**
     * @param clazz class
     * @param methodName method name
     * @return method
     */
    @Nullable
    public Method getProvideTargetMethod(Class<?> clazz, String methodName) {
        Method method = provideTargetMethodsCache.getUnchecked(new MethodTag(clazz, methodName));
        if (method == NO_METHOD_VALUE) {
            return null;
        }

        return method;
    }

    public List<AnnotatedMethod<Provide>> getAnnotatedProvideMethods(Class<?> clazz) {
        return provideMethodsCache.getUnchecked(clazz);
    }

    public List<AnnotatedMethod<Subscribe>> getAnnotatedSubscribeMethods(Class<?> clazz) {
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

    protected List<AnnotatedMethod<Provide>> getAnnotatedProvideMethodsNotCached(Class<?> clazz) {
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);

        List<AnnotatedMethod<Provide>> annotatedMethods = new ArrayList<>();

        for (Method m : methods) {
            Provide provideAnnotation = findMergedAnnotation(m, Provide.class);
            if (provideAnnotation != null) {
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
                annotatedMethods.add(new AnnotatedMethod<>(provideAnnotation, m));
            }
        }

        return ImmutableList.copyOf(annotatedMethods);
    }

    protected List<AnnotatedMethod<Subscribe>> getAnnotatedSubscribeMethodsNotCached(Class<?> clazz) {
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);

        List<AnnotatedMethod<Subscribe>> annotatedMethods = new ArrayList<>();

        for (Method m : methods) {
            if (m.getParameterCount() == 1 && EventObject.class.isAssignableFrom(m.getParameterTypes()[0])) {
                Subscribe annotation = findMergedAnnotation(m, Subscribe.class);
                if (annotation != null) {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                    annotatedMethods.add(new AnnotatedMethod<>(annotation, m));
                }
            }
        }

        annotatedMethods.sort(this::compareSubscribeMethods);

        return ImmutableList.copyOf(annotatedMethods);
    }

    protected int compareSubscribeMethods(AnnotatedMethod<Subscribe> am1, AnnotatedMethod<Subscribe> am2) {
        Method m1 = am1.getMethod();
        Method m2 = am2.getMethod();

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

    protected Method getProvideTargetMethodNotCached(MethodTag methodTag) {
        for (Method method : ReflectionUtils.getUniqueDeclaredMethods(methodTag.getClazz())) {
            if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(methodTag.getMethodName())) {
                return method;
            }
        }

        return NO_METHOD_VALUE;
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

    public static class AnnotatedMethod<T> {

        private final T annotation;
        private final Method method;

        public AnnotatedMethod(T annotation, Method method) {
            this.annotation = annotation;
            this.method = method;
        }

        public T getAnnotation() {
            return annotation;
        }

        public Method getMethod() {
            return method;
        }
    }

    public static class MethodTag {
        private final Class<?> clazz;
        private final String methodName;

        public MethodTag(Class<?> clazz, String methodName) {
            this.clazz = clazz;
            this.methodName = methodName;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public String getMethodName() {
            return methodName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MethodTag methodTag = (MethodTag) o;
            return Objects.equals(clazz, methodTag.clazz) &&
                    Objects.equals(methodName, methodTag.methodName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, methodName);
        }

        @Override
        public String toString() {
            return "MethodTag{" +
                    "clazz=" + clazz +
                    ", methodName='" + methodName + '\'' +
                    '}';
        }
    }
}