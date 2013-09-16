/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * DEPRECATED - use {@link MetadataTools}
 */
@Deprecated
public abstract class MetadataHelper {

    /**
     * DEPRECATED - use {@link MetadataTools#isCascade(com.haulmont.chile.core.model.MetaProperty)}
     */
    @Deprecated
    public static boolean isCascade(MetaProperty metaProperty) {
        return AppBeans.get(MetadataTools.class).isCascade(metaProperty);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#isSystem(com.haulmont.chile.core.model.MetaProperty)}
     */
    @Deprecated
    public static boolean isSystem(MetaProperty metaProperty) {
        return AppBeans.get(MetadataTools.class).isSystem(metaProperty);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#isPersistent(com.haulmont.chile.core.model.MetaPropertyPath)}
     */
    @Deprecated
    public static boolean isPersistent(MetaPropertyPath metaPropertyPath) {
        return AppBeans.get(MetadataTools.class).isPersistent(metaPropertyPath);
    }

    /**
     * DEPRECATED - use {@link MetadataHelper#isPersistent(com.haulmont.chile.core.model.MetaPropertyPath)}
     */
    @Deprecated
    public static boolean isPersistent(MetaProperty metaProperty) {
        return AppBeans.get(MetadataTools.class).isPersistent(metaProperty);
    }

    /**
     * DEPRECATED - use {@link MetadataHelper#isEmbedded(com.haulmont.chile.core.model.MetaProperty)}
     */
    @Deprecated
    public static boolean isEmbedded(MetaProperty metaProperty) {
        return AppBeans.get(MetadataTools.class).isEmbedded(metaProperty);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#getPropertyPaths(com.haulmont.chile.core.model.MetaClass)}
     */
    @Deprecated
    public static Collection<MetaPropertyPath> getPropertyPaths(MetaClass metaClass) {
        return AppBeans.get(MetadataTools.class).getPropertyPaths(metaClass);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#getViewPropertyPaths(com.haulmont.cuba.core.global.View, com.haulmont.chile.core.model.MetaClass)}
     */
    @Deprecated
    public static Collection<MetaPropertyPath> getViewPropertyPaths(View view, MetaClass metaClass) {
        return AppBeans.get(MetadataTools.class).getViewPropertyPaths(view, metaClass);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#toPropertyPaths(java.util.Collection<com.haulmont.chile.core.model.MetaProperty>)}
     */
    @Deprecated
    public static Collection<MetaPropertyPath> toPropertyPaths(Collection<MetaProperty> properties) {
        return AppBeans.get(MetadataTools.class).toPropertyPaths(properties);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#getNamePatternProperties(com.haulmont.chile.core.model.MetaClass)}
     */
    @Deprecated
    public static Collection<MetaProperty> getNamePatternProperties(MetaClass metaClass) {
        return AppBeans.get(MetadataTools.class).getNamePatternProperties(metaClass);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#viewContainsProperty(com.haulmont.cuba.core.global.View, com.haulmont.chile.core.model.MetaPropertyPath)}
     */
    @Deprecated
    public static boolean viewContainsProperty(View view, MetaPropertyPath propertyPath) {
        return AppBeans.get(MetadataTools.class).viewContainsProperty(view, propertyPath);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#isAnnotationPresent(java.lang.Object, java.lang.String, java.lang.Class<? extends java.lang.annotation.Annotation>)}
     */
    @Deprecated
    public static boolean isAnnotationPresent(Object object, String property,
                                              Class<? extends Annotation> annotationClass) {
        return AppBeans.get(MetadataTools.class).isAnnotationPresent(object, property, annotationClass);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#isAnnotationPresent(java.lang.Object, java.lang.String, java.lang.Class<? extends java.lang.annotation.Annotation>)}
     */
    @Deprecated
    public static boolean isAnnotationPresent(Class javaClass, String property,
                                              Class<? extends Annotation> annotationClass) {
        return AppBeans.get(MetadataTools.class).isAnnotationPresent(javaClass, property, annotationClass);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#isTransient(java.lang.Object, java.lang.String)}
     */
    @Deprecated
    public static boolean isTransient(Object object, String property) {
        return AppBeans.get(MetadataTools.class).isTransient(object, property);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#isTransient(java.lang.Object, java.lang.String)}
     */
    @Deprecated
    public static boolean isTransient(MetaProperty metaProperty) {
        return AppBeans.get(MetadataTools.class).isTransient(metaProperty);
    }

    /**
     * DEPRECATED - use {@link com.haulmont.chile.core.model.Session#getClasses()}
     */
    @Deprecated
    public static Collection<MetaClass> getAllMetaClasses() {
        return AppBeans.get(Metadata.class).getSession().getClasses();
    }

    /**
     * DEPRECATED - use {@link MetadataTools#getAllPersistentMetaClasses()}
     */
    @Deprecated
    public static Collection<MetaClass> getAllPersistentMetaClasses() {
        return AppBeans.get(MetadataTools.class).getAllPersistentMetaClasses();
    }

    /**
     * DEPRECATED - use {@link MetadataTools#getAllEmbeddableMetaClasses()}
     */
    @Deprecated
    public static Collection<MetaClass> getAllEmbeddableMetaClasses() {
        return AppBeans.get(MetadataTools.class).getAllEmbeddableMetaClasses();
    }

    /**
     * DEPRECATED - use {@link MetadataTools#getAllEnums()}
     */
    @Deprecated
    public static Collection<Class> getAllEnums() {
        return AppBeans.get(MetadataTools.class).getAllEnums();
    }
}
