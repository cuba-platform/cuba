/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: chernov
 * Created: 22.11.2010 14:43:00
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.entity.BaseLongIdEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.persistence.*;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;

public abstract class MetadataHelper {

    private static volatile Collection<MetaClass> metaClasses;
    private static volatile Collection<Class> enums;

    public static Class getTypeClass(MetaProperty metaProperty) {
        if (metaProperty == null)
            throw new IllegalArgumentException("MetaProperty is null");

        final Range range = metaProperty.getRange();
        if (range.isDatatype()) {
            return range.asDatatype().getJavaClass();
        } else if (range.isClass()) {
            return range.asClass().getJavaClass();
        } else if (range.isEnum()) {
            return range.asEnumeration().getJavaClass();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean isCascade(MetaProperty metaProperty) {
        OneToMany oneToMany = metaProperty.getAnnotatedElement().getAnnotation(OneToMany.class);
        if (oneToMany != null) {
            final Collection<CascadeType> cascadeTypes = Arrays.asList(oneToMany.cascade());
            if (cascadeTypes.contains(CascadeType.ALL) ||
                    cascadeTypes.contains(CascadeType.MERGE)) {
                return true;
            }
        }
        ManyToMany manyToMany = metaProperty.getAnnotatedElement().getAnnotation(ManyToMany.class);
        if (manyToMany != null && StringUtils.isBlank(manyToMany.mappedBy())) {
            return true;
        }
        return false;
    }

    public static boolean isSystem(MetaProperty metaProperty) {
        final MetaClass metaClass = metaProperty.getDomain();
        final Class javaClass = metaClass.getJavaClass();

        return BaseUuidEntity.class.equals(javaClass) ||
                StandardEntity.class.equals(javaClass) ||
                BaseLongIdEntity.class.equals(javaClass);
    }

    public static boolean isPersistent(MetaPropertyPath metaPropertyPath) {
        for (MetaProperty metaProperty : metaPropertyPath.getMetaProperties()) {
            if (metaProperty.getAnnotatedElement().isAnnotationPresent(com.haulmont.chile.core.annotations.MetaProperty.class))
                return false;
        }

        return true;
    }

    public static boolean isEmbedded(MetaProperty metaProperty){
        return metaProperty.getAnnotatedElement().getAnnotation(Embedded.class) != null;
    }

    public static Collection<MetaPropertyPath> getPropertyPaths(MetaClass metaClass) {
        List<MetaPropertyPath> res = new ArrayList<MetaPropertyPath>();
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            res.add(new MetaPropertyPath(metaClass, metaProperty));
        }

        return res;
    }

    public static Collection<MetaPropertyPath> getViewPropertyPaths(View view, MetaClass metaClass) {
        List<MetaPropertyPath> propertyPaths = new ArrayList<MetaPropertyPath>(metaClass.getProperties().size());
        for (final MetaProperty metaProperty : metaClass.getProperties()) {
            final MetaPropertyPath metaPropertyPath = new MetaPropertyPath(metaClass, metaProperty);
            if (viewContainsProperty(view, metaPropertyPath)) {
                propertyPaths.add(metaPropertyPath);
            }
        }
        return propertyPaths;
    }

    public static Collection<MetaPropertyPath> toPropertyPaths(Collection<MetaProperty> properties) {
        List<MetaPropertyPath> res = new ArrayList<MetaPropertyPath>();
        for (MetaProperty metaProperty : properties) {
            res.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
        }

        return res;
    }

    /**
     * Visit all properties of an object graph starting from the specified instance
     */
    public static void walkProperties(Instance instance, PropertyVisitor visitor) {
        Session metadata = MetadataProvider.getSession();
        __walkProperties(instance, visitor, metadata, new HashSet<Instance>());
    }

    private static void __walkProperties(Instance instance, PropertyVisitor visitor,
                                         Session metadata, Set<Instance> visited) {
        if (visited.contains(instance))
            return;
        visited.add(instance);

        MetaClass metaClass = metadata.getClass(instance.getClass());
        if (metaClass == null)
            return;

        Collection<MetaProperty> properties = metaClass.getProperties();
        for (MetaProperty property : properties) {

            visitor.visit(instance, property);

            Object value = instance.getValue(property.getName());
            if (value != null && property.getRange().isClass()) {
                if (property.getRange().getCardinality().isMany()) {
                    Collection collection = (Collection) value;
                    for (Object o : collection) {
                        if (o instanceof Instance)
                            __walkProperties((Instance) o, visitor, metadata, visited);
                    }
                } else if (value instanceof Instance) {
                    __walkProperties((Instance) value, visitor, metadata, visited);
                }
            }
        }
    }

    public static boolean viewContainsProperty(View view, MetaPropertyPath propertyPath) {
        View currentView = view;
        for (MetaProperty metaProperty : propertyPath.get()) {
            if (currentView == null) return false;

            final ViewProperty property = currentView.getProperty(metaProperty.getName());
            if (property == null) return false;

            currentView = property.getView();
        }
        return true;
    }

    public static boolean isAnnotationPresent(Object object, String property,
                                              Class<? extends Annotation> annotationClass) {
        return isAnnotationPresent(object.getClass(), property, annotationClass);
    }

    public static boolean isAnnotationPresent(Class javaClass, String property,
                                              Class<? extends Annotation> annotationClass) {
        Field field;
        try {
            field = javaClass.getDeclaredField(property);
            return field.isAnnotationPresent(annotationClass);
        } catch (NoSuchFieldException e) {
            Class superclass = javaClass.getSuperclass();
            while (superclass != null) {
                try {
                    field = superclass.getDeclaredField(property);
                    return field.isAnnotationPresent(annotationClass);
                } catch (NoSuchFieldException e1) {
                    superclass = superclass.getSuperclass();
                }
            }
            throw new RuntimeException("Property not found: " + property);
        }
    }

    public static boolean isTransient(Object object, String property) {
        return isAnnotationPresent(object, property, Transient.class);
    }

    public static boolean isTransient(MetaProperty metaProperty) {
        boolean isMetaProperty = isAnnotationPresent(metaProperty.getAnnotatedElement(),
                com.haulmont.chile.core.annotations.MetaProperty.class);
        return isMetaProperty || isAnnotationPresent(metaProperty.getAnnotatedElement(), Transient.class);
    }

    private static boolean isAnnotationPresent(AnnotatedElement annotatedElement,
                                               Class<? extends Annotation> annotationClass) {
        return annotatedElement.isAnnotationPresent(annotationClass);
    }

    public static void deployViews(Element rootFrameElement) {
        final Element metadataContextElement = rootFrameElement.element("metadataContext");
        if (metadataContextElement != null) {
            @SuppressWarnings({"unchecked"})
            List<Element> fileElements = metadataContextElement.elements("deployViews");
            for (Element fileElement : fileElements) {
                final String resource = fileElement.attributeValue("name");
                InputStream resourceInputStream = ScriptingProvider.getResourceAsStream(resource);
                if (resourceInputStream == null) {
                    throw new RuntimeException("View resource not found: " + ((resource == null) ? "[null]" : resource));
                }
                try {
                    MetadataProvider.getViewRepository().deployViews(resourceInputStream);
                } finally {
                    IOUtils.closeQuietly(resourceInputStream);
                }
            }

            @SuppressWarnings({"unchecked"})
            List<Element> viewElements = metadataContextElement.elements("view");
            for (Element viewElement : viewElements) {
                MetadataProvider.getViewRepository().deployView(metadataContextElement, viewElement);
            }
        }
    }

    public static Collection<MetaClass> getAllMetaClasses() {
        if (metaClasses == null) {
            synchronized (MetadataHelper.class) {
                metaClasses = MetadataProvider.getSession().getClasses();
            }
        }
        return metaClasses;
    }

    public static Collection<MetaClass> getAllPersistentMetaClasses() {
        List<MetaClass> result = new ArrayList<MetaClass>();
        for (MetaClass metaClass : getAllMetaClasses()) {
            if (metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Entity.class)) {
                result.add(metaClass);
            }
        }
        return result;
    }

    public static Collection<Class> getAllEnums() {
        if (enums == null) {
            synchronized (MetadataHelper.class) {
                enums = new HashSet<Class>();
                for (MetaClass metaClass : getAllMetaClasses()) {
                    for (MetaProperty metaProperty : metaClass.getProperties()) {
                        if (metaProperty.getRange() != null && metaProperty.getRange().isEnum()) {
                            Class c = metaProperty.getRange().asEnumeration().getJavaClass();
                            enums.add(c);
                        }
                    }
                }
            }
        }
        return enums;
    }
}
