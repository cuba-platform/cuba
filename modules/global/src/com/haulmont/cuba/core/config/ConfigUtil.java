/*
 * A High-Level Framework for Application Configuration
 *
 * Copyright 2007 Merlin Hughes / Learning Objects, Inc.
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

package com.haulmont.cuba.core.config;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.type.TypeFactory;
import com.haulmont.cuba.core.config.type.TypeStringify;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various configuration utility methods.
 *
 */
public final class ConfigUtil {
    /**
     * Prohibited.
     */
    private ConfigUtil() {
    }

    /**
     * Regex matching a getter (is* or get*). The second group is the field name.
     */
    static final Pattern GET_RE = Pattern.compile("^(is|get)(.+)$");
    /**
     * Regex matching a setter (set*). The second group is the field name.
     */
    static final Pattern SET_RE = Pattern.compile("^(set)(.+)$");
    /**
     * Regex matching an accessor (is*, get* or set*). The second group is the field name.
     */
    static final Pattern ACCESS_RE = Pattern.compile("^(is|get|set)(.+)$");
    /**
     * Regex matching an add listener method (add*Listener). The second group is the field name.
     */
    static final Pattern ADD_LISTENER_RE = Pattern.compile("^(add)(.*)Listener$");
    /**
     * Regex matching a remove listener method (remove*Listener). The second group is the field name.
     */
    static final Pattern REMOVE_LISTENER_RE = Pattern.compile("^(remove)(.*)Listener$");
    /**
     * Regex matching a listener method (add*Listener or remove*Listener). The second group is the field name.
     */
    static final Pattern LISTENER_RE = Pattern.compile("^(add|remove)(.*)Listener$");

    /**
     * Uncapitalize a string with support for leading acronyms. This
     * supports pretty uncapitalization of strings such as "URL" (to
     * "url") and "URLDecoder" (to "urlDecoder"). If a string begins with
     * a sequence of capital letters, all but the last are uncapitalized,
     * except in the case that the entire string is capitalized or the
     * capitals are followed by a non-letter, in which case all are
     * uncapitalized.
     *
     * @param str The string.
     * @return The uncapitalized string.
     */
    public static String extendedUncapitalize(String str) {
        // fooBar -> fooBar
        // FooBar -> fooBar
        // FOOBar -> fooBar
        // FOOBAR -> foobar
        // FOO8ar -> foo8ar
        int index = 0, length = str.length();
        while ((index < length) &&
                Character.isUpperCase(str.charAt(index)) &&
                ((index == 0) || (index == length - 1) ||
                        !Character.isLowerCase(str.charAt(index + 1))))
        {
            ++index;
        }
        return str.substring(0, index).toLowerCase() + str.substring(index);
    }

    /**
     * Get the property name associated with a configuration interface method.
     * If a {@link Property} annotation is present, that value is returned.
     * Otherwise the {@link #getPropertyPrefix interface prefix} is concatenated
     * with the {@link #extendedUncapitalize uncapitalized} property name.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     * @return The property name.
     */
    public static String getPropertyName(Class<?> configInterface, Method method) {
        Property property = getAnnotation(configInterface, method, Property.class, false);
        if (property != null) {
            return property.value();
        } else {
            String prefix = getPropertyPrefix(configInterface);
            String methodName = method.getName();
            Matcher matcher;
            if (((matcher = ACCESS_RE.matcher(methodName)).matches() ||
                    (matcher = LISTENER_RE.matcher(methodName)).matches()) &&
                    !"".equals(matcher.group(2)))
            {
                String prop = matcher.group(2);
                return prefix + extendedUncapitalize(prop);
            } else {
                throw new IllegalArgumentException("Unsupported method name: " + method);
            }
        }
    }

    /**
     * Get the prefix associated with a configuration interface.
     * If the interface has an enclosing class, the
     * fully-qualified name of that class is used, or else the name of the
     * package containing the interface. In either of the latter two
     * cases, a '.' is appended to the name.
     *
     * @param configInterface The configuration interface.
     * @return The interface prefix.
     */
    public static String getPropertyPrefix(Class<?> configInterface) {
        // Foo -> ""
        // foo.Bar -> "foo."
        // foo.Bar$Baz -> "foo.Bar."
        Class<?> enclosingClass = configInterface.getEnclosingClass();
        if (enclosingClass != null) {
            return enclosingClass.getName() + '.';
        } else {
            Package pkg = configInterface.getPackage();
            if (pkg != null) {
                return pkg.getName() + '.';
            } else {
                return "";
            }
        }
    }

    /**
     * Get the type of a method. If the method has a non-void return
     * type, that type is returned. Otherwise if the method has at least
     * one parameter, the type of the first parameter is returned.
     *
     * @param method The method.
     * @return The method type, or else {@link Void#TYPE}.
     */
    public static Class<?> getMethodType(Method method) {
        Class<?> methodType = method.getReturnType();
        if (Void.TYPE.equals(methodType)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length > 0) {
                methodType = parameterTypes[0];
            }
        }
        return methodType;
    }

    /**
     * Get the plain get method associated with a configuration interface
     * method. This is the unparameterized getter associated with the same
     * field as the specified configuration method. For example,
     * isDisabled() might be returned for the method
     * addDisabledListener().
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     * @return The plain get method, or else null.
     */
    public static Method getGetMethod(Class<?> configInterface, Method method) {
        Method getMethod = null;
        String methodName = method.getName();
        Matcher matcher;
        if ((matcher = ACCESS_RE.matcher(methodName)).matches() ||
                (matcher = LISTENER_RE.matcher(methodName)).matches())
        {
            String prop = matcher.group(2);
            try {
                getMethod = configInterface.getMethod("get" + prop);
            } catch (NoSuchMethodException ex) {
                Class<?> methodType = getMethodType(method);
                try {
                    getMethod = configInterface.getMethod("get" + prop, methodType);
                } catch (NoSuchMethodException ex2) {
                    if (Boolean.TYPE.equals(methodType)) {
                        try {
                            getMethod = configInterface.getMethod("is" + prop);
                        } catch (NoSuchMethodException ex3) {
                            try {
                                getMethod = configInterface.getMethod("is" + prop, methodType);
                            } catch (NoSuchMethodException ex4) {
                                // nothing
                            }
                        }
                    }
                }
            }
        }
        return getMethod;
    }

    /**
     * Search for an annotation on a configuration interface method. In
     * addition to searching the method itself, the {@link #getGetMethod
     * plain get method} is also searched, as can the {@link
     * #getMethodType method type} be.
     *
     * @param configInterface  The configuration interface.
     * @param method           The method.
     * @param annotationType   The annotation type of interest.
     * @param searchMethodType Whether to search the method type.
     * @return The annotation, or null.
     */
    public static <T extends Annotation> T getAnnotation(Class<?> configInterface, Method method, Class<T> annotationType, boolean searchMethodType) {
        T annotation = method.getAnnotation(annotationType);
        if (annotation == null) {
            Method getMethod = getGetMethod(configInterface, method);
            if (getMethod != null) {
                annotation = getMethod.getAnnotation(annotationType);
            }
            if ((annotation == null) && searchMethodType) {
                String methodName = method.getName();
                if (ACCESS_RE.matcher(methodName).matches()) {
                    // Is the annotation present on the method type?
                    Class<?> methodType = getMethodType(method);
                    annotation = methodType.getAnnotation(annotationType);
                }
            }
        }
        return annotation;
    }

    /**
     * The value indicating that no default was specified.
     */
    public static final String NO_DEFAULT = new String();

    /**
     * Get the default value of a configuration interface method. If a
     * {@link Default} annotation is present then that string is converted
     * to the appropriate type using the {@link TypeFactory} class.
     * Otherwise, for the type Foo, this searches for a FooDefault
     * annotation. If such an annotation is present then its value is
     * returned.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     * @return The default value, or null.
     */
    public static String getDefaultValue(Class<?> configInterface, Method method) {
        // TODO: returnType.cast()?
        try {
            Default defaultValue = method.getAnnotation(Default.class);
            if (defaultValue != null) {
                return defaultValue.value();
            } else {
                Class<?> type = method.getReturnType();
                if (EnumClass.class.isAssignableFrom(type)) {
                    @SuppressWarnings("unchecked")
                    Class<EnumClass> enumeration = (Class<EnumClass>) type;
                    EnumStore mode = getAnnotation(configInterface, method, EnumStore.class, true);
                    if (mode != null && EnumStoreMode.ID == mode.value()) {
                        Class<?> idType = getEnumIdType(enumeration);
                        String name = "Default" + StringUtils.capitalize(ClassUtils.getShortClassName(idType));
                        Object value = getAnnotationValue(method, name);
                        if (value != null) {
                            Method fromId = enumeration.getDeclaredMethod("fromId", idType);
                            TypeStringify stringConverter = TypeStringify.getInstance(configInterface, method);
                            return stringConverter.stringify(fromId.invoke(null, value));
                        }
                        return NO_DEFAULT;
                    }
                }
                String name = "Default" + StringUtils.capitalize(ClassUtils.getShortClassName(type));
                Object value = getAnnotationValue(method, name);
                if (value != null) {
                    TypeStringify stringConverter = TypeStringify.getInstance(configInterface, method);
                    return stringConverter.stringify(value);
                }
            }
            return NO_DEFAULT;
        } catch (Exception ex) {
            throw new RuntimeException("Default value error", ex);
        }
    }

    @Nullable
    private static Object getAnnotationValue(AnnotatedElement annotated, String name) throws ReflectiveOperationException {
        for (Annotation annotation : annotated.getAnnotations()) {
            Class annotationType = annotation.annotationType();
            if (name.equals(ClassUtils.getShortClassName(annotationType))) {
                Method method = annotationType.getMethod("value");
                return method.invoke(annotation);
            }
        }
        return null;
    }

    public static SourceType getSourceType(Class<?> configInterface, Method method) {
        Source source = method.getAnnotation(Source.class);
        if (source == null) {
            Method getMethod = getGetMethod(configInterface, method);
            if (getMethod != null && !method.equals(getMethod)) {
                source = getMethod.getAnnotation(Source.class);
            }
            if (source == null) {
                source = configInterface.getAnnotation(Source.class);
                if (source == null)
                    return SourceType.DATABASE;
            }
        }
        return source.type();
    }

    /**
     * Returns id type parameter for classes that implement {@link EnumClass}.
     * Id type is usually known for concrete enum classes, but if type is unavailable,
     * {@code IllegalArgumentException} is thrown.
     *
     * @param enumeration class describing subclass of {@code EnumClass}
     * @return id type parameter
     */
    public static Class<?> getEnumIdType(Class<? extends EnumClass> enumeration) {
        try {
            return enumeration.getMethod("getId").getReturnType();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot infer generic type parameter for " + enumeration.getName() +
                    ". Please check if enumeration has public method getId with proper return type");
        }
    }
}