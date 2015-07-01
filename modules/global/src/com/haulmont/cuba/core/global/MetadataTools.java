/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.entity.Versioned;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Utility class to provide common metadata-related functionality.
 * <p/> Implemented as Spring bean to allow extension in application projects.
 * <p/> A reference to this class can be obtained either via DI or by {@link com.haulmont.cuba.core.global.Metadata#getTools()}.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(MetadataTools.NAME)
public class MetadataTools {

    public static final String NAME = "cuba_MetadataTools";

    @Inject
    protected Metadata metadata;

    @Inject
    protected Messages messages;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected DatatypeFormatter datatypeFormatter;

    protected volatile Collection<Class> enums;

    /**
     * Default constructor used by container at runtime and in server-side integration tests.
     */
    public MetadataTools() {
    }

    /**
     * Formats a value according to the property type.
     * @param value    object to format
     * @param property metadata
     * @return formatted value as string
     */
    public String format(@Nullable Object value, MetaProperty property) {
        if (value == null)
            return "";
        Objects.requireNonNull(property, "property is null");

        Range range = property.getRange();
        if (range.isDatatype()) {
            Datatype datatype = range.asDatatype();
            if (value instanceof Date && datatype.getName().equals(DateTimeDatatype.NAME)) {
                Object ignoreUserTimeZone = property.getAnnotations().get(IgnoreUserTimeZone.class.getName());
                if (!Boolean.TRUE.equals(ignoreUserTimeZone)) {
                    return datatypeFormatter.formatDateTime((Date) value);
                }
            }
            //noinspection unchecked
            return datatype.format(value, userSessionSource.getLocale());
        } else if (range.isEnum()) {
            return messages.getMessage((Enum) value);
        } else if (value instanceof Instance) {
            return ((Instance) value).getInstanceName();
        } else {
            return value.toString();
        }
    }

    /**
     * Formats a value according to the value type.
     * @param value    object to format
     * @return formatted value as string
     */
    public String format(@Nullable Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Instance) {
            return ((Instance) value).getInstanceName();
        } else if (value instanceof EnumClass && value instanceof Enum) {
            return messages.getMessage((Enum) value, userSessionSource.getLocale());
        } else {
            Datatype datatype = Datatypes.get(value.getClass());
            if (datatype != null) {
                return datatype.format(value, userSessionSource.getLocale());
            }

            return value.toString();
        }
    }

    /**
     * @return name of a primary key attribute, or null if the entity has no primary key (e.g. embeddable)
     */
    @Nullable
    public String getPrimaryKeyName(MetaClass metaClass) {
        String pkProperty = (String) metaClass.getAnnotations().get("primaryKey");
        if (pkProperty != null) {
            return pkProperty;
        } else {
            MetaClass ancestor = metaClass.getAncestor();
            while (ancestor != null) {
                pkProperty = (String) ancestor.getAnnotations().get("primaryKey");
                if (pkProperty != null)
                    return pkProperty;
                ancestor = ancestor.getAncestor();
            }
        }
        return null;
    }

    /**
     * @return MetaProperty representing a primary key attribute, or null if the entity has no primary key (e.g. embeddable)
     */
    @Nullable
    public MetaProperty getPrimaryKeyProperty(MetaClass metaClass) {
        String primaryKeyName = getPrimaryKeyName(metaClass);
        return primaryKeyName == null ? null : metaClass.getPropertyNN(primaryKeyName);
    }

    /**
     * Determine whether an object denoted by the given property is merged into persistence context together with the
     * owning object. This is true if the property is ManyToMany, or if it is OneToMany with certain CasacdeType defined.
     */
    public boolean isCascade(MetaProperty metaProperty) {
        Objects.requireNonNull(metaProperty, "metaProperty is null");
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

    /**
     * Determine whether the given property is system-level. A property is considered system if it is defined not
     * in an entity class but in one of its base interfaces:
     * {@link BaseEntity}, {@link Updatable}, {@link SoftDelete}, {@link Versioned}
     */
    public boolean isSystem(MetaProperty metaProperty) {
        Objects.requireNonNull(metaProperty, "metaProperty is null");
        return Boolean.TRUE.equals(metaProperty.getAnnotations().get("system"));
    }

    /**
     * Determine whether all the properties defined by the given property path are persistent.
     * @see #isPersistent(com.haulmont.chile.core.model.MetaProperty)
     */
    public boolean isPersistent(MetaPropertyPath metaPropertyPath) {
        Objects.requireNonNull(metaPropertyPath, "metaPropertyPath is null");
        for (MetaProperty metaProperty : metaPropertyPath.getMetaProperties()) {
            if (!isPersistent(metaProperty))
                return false;
        }
        return true;
    }

    /**
     * Determine whether the given property is persistent, that is stored in the database.
     */
    public boolean isPersistent(MetaProperty metaProperty) {
        Objects.requireNonNull(metaProperty, "metaProperty is null");
        return Boolean.TRUE.equals(metaProperty.getAnnotations().get("persistent"));
    }

    /**
     * Determine whether the given property is transient, that is not stored in the database.
     * <p/>Unlike {@link #isTransient(com.haulmont.chile.core.model.MetaProperty)} for objects and properties not
     * registered in metadata this method returns <code>true</code>.
     * @param object   entity instance
     * @param property property name
     */
    public boolean isTransient(Object object, String property) {
        Objects.requireNonNull(object, "object is null");
        MetaClass metaClass = metadata.getSession().getClass(object.getClass());
        if (metaClass == null)
            return true;
        MetaProperty metaProperty = metaClass.getProperty(property);
        return metaProperty == null || !isPersistent(metaProperty);
    }

    /**
     * Determine whether the given property is transient, that is not stored in the database.
     */
    public boolean isTransient(MetaProperty metaProperty) {
        return !isPersistent(metaProperty);
    }

    /**
     * Determine whether the given property denotes an embedded object.
     * @see Embedded
     */
    public boolean isEmbedded(MetaProperty metaProperty) {
        Objects.requireNonNull(metaProperty, "metaProperty is null");
        return metaProperty.getAnnotatedElement().isAnnotationPresent(Embedded.class);
    }

    /**
     * Determine whether the given property is on the owning side of an association.
     */
    public boolean isOwningSide(MetaProperty metaProperty) {
        Preconditions.checkNotNullArgument(metaProperty, "metaProperty is null");
        if (!metaProperty.getRange().isClass())
            return false;

        AnnotatedElement el = metaProperty.getAnnotatedElement();
        for (Annotation annotation : el.getAnnotations()) {
            if (annotation instanceof ManyToOne)
                return true;
            if (annotation instanceof OneToMany || annotation instanceof OneToOne)
                return el.isAnnotationPresent(JoinColumn.class) || el.isAnnotationPresent(JoinTable.class);
            if (annotation instanceof ManyToMany)
                return el.isAnnotationPresent(JoinTable.class);
        }

        return false;
    }

    /**
     * Determine whether the given entity is marked as {@link SystemLevel}.
     */
    public boolean isSystemLevel(MetaClass metaClass) {
        Objects.requireNonNull(metaClass, "metaClass is null");
        Boolean systemLevel = (Boolean) metaClass.getAnnotations().get(SystemLevel.class.getName());
        if (systemLevel == null) {
            String propagateKey = SystemLevel.class.getName() + SystemLevel.PROPAGATE;

            for (MetaClass aClass : metaClass.getAncestors()) {
                Boolean propagate = (Boolean) aClass.getAnnotations().get(propagateKey);
                if (BooleanUtils.isFalse(propagate)) {
                    // in case of non propagated SystemLevel, get it from original entity
                    MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                    if (originalMetaClass != null) {
                        systemLevel = (Boolean) originalMetaClass.getAnnotations().get(SystemLevel.class.getName());
                    }
                    break;
                }

                systemLevel = (Boolean) aClass.getAnnotations().get(SystemLevel.class.getName());
                if (systemLevel != null)
                    break;
            }
        }
        return systemLevel == null ? false : systemLevel;
    }

    /**
     * Determine whether the given property is marked as {@link SystemLevel}.
     */
    public boolean isSystemLevel(MetaProperty metaProperty) {
        Objects.requireNonNull(metaProperty, "metaProperty is null");
        Boolean systemLevel = (Boolean) metaProperty.getAnnotations().get(SystemLevel.class.getName());
        return systemLevel == null ? false : systemLevel;
    }

    /**
     * Determine whether the given annotation is present in the object's class or in any of its superclasses.
     * @param object          entity instance
     * @param property        property name
     * @param annotationClass annotation class
     */
    public boolean isAnnotationPresent(Object object, String property, Class<? extends Annotation> annotationClass) {
        Objects.requireNonNull(object, "object is null");
        return isAnnotationPresent(object.getClass(), property, annotationClass);
    }

    /**
     * Determine whether the given annotation is present in the object's class or in any of its superclasses.
     * @param javaClass       entity class
     * @param property        property name
     * @param annotationClass annotation class
     * @return
     */
    public boolean isAnnotationPresent(Class javaClass, String property, Class<? extends Annotation> annotationClass) {
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

    /**
     * Determine whether the given metaclass is persistent, that is stored in the database.
     */
    public boolean isPersistent(MetaClass metaClass) {
        return metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Entity.class);
    }

    /**
     * Determine whether the given class is persistent, that is stored in the database.
     */
    public boolean isPersistent(Class aClass) {
        return aClass.isAnnotationPresent(javax.persistence.Entity.class);
    }

    /**
     * Determine whether the given metaclass is embeddable.
     */
    public boolean isEmbeddable(MetaClass metaClass) {
        Class javaClass;
        try {
            // RuntimePropertiesMetaClass can throw UnsupportedOperationException
            javaClass = metaClass.getJavaClass();
        } catch (UnsupportedOperationException e) {
            return false;
        }

        return javaClass.isAnnotationPresent(javax.persistence.Embeddable.class);
    }

    /**
     * Get metaclass that contains metaproperty for passed propertyPath.
     * Resolves real metaclass for property in consideration of inherited entity classes and extended classes.
     *
     * @param propertyPath Property path
     * @return metaclass
     */
    public MetaClass getPropertyEnclosingMetaClass(MetaPropertyPath propertyPath) {
        checkNotNullArgument(propertyPath, "Property path should not be null");

        MetaProperty[] propertyChain = propertyPath.getMetaProperties();
        if (propertyChain.length > 1) {
            MetaProperty chainProperty = propertyChain[propertyChain.length - 2];
            return chainProperty.getRange().asClass();
        } else {
            return propertyPath.getMetaClass();
        }
    }

    /**
     * Return a collection of properties included into entity's name pattern (see {@link NamePattern}).
     * @param metaClass entity metaclass
     * @return collection of the name pattern properties
     */
    @Nonnull
    public Collection<MetaProperty> getNamePatternProperties(MetaClass metaClass) {
        return getNamePatternProperties(metaClass, false);
    }

    /**
     * Return a collection of properties included into entity's name pattern (see {@link NamePattern}).
     * @param metaClass   entity metaclass
     * @param useOriginal if true, and if the given metaclass doesn't define a {@link NamePattern} and if it is an
     *                    extended entity, this method tries to find a name pattern in an original entity
     * @return collection of the name pattern properties
     */
    @Nonnull
    public Collection<MetaProperty> getNamePatternProperties(MetaClass metaClass, boolean useOriginal) {
        Collection<MetaProperty> properties = new ArrayList<>();
        String pattern = (String) metaClass.getAnnotations().get(NamePattern.class.getName());
        if (pattern == null && useOriginal) {
            MetaClass original = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
            if (original != null) {
                pattern = (String) original.getAnnotations().get(NamePattern.class.getName());
            }
        }
        if (!StringUtils.isBlank(pattern)) {
            String value = StringUtils.substringAfter(pattern, "|");
            String[] fields = StringUtils.splitPreserveAllTokens(value, ",");
            for (String field : fields) {
                String fieldName = StringUtils.trim(field);

                MetaProperty property = metaClass.getProperty(fieldName);
                if (property != null) {
                    properties.add(metaClass.getProperty(fieldName));
                } else {
                    throw new DevelopmentException(
                            String.format("Property '%s' is not found in %s", field, metaClass.toString()),
                            "NamePattern", pattern);
                }
            }
        }
        return properties;
    }

    /**
     * @return collection of properties owned by this metaclass and all its ancestors in the form of
     * {@link MetaPropertyPath}s containing one property each
     */
    public Collection<MetaPropertyPath> getPropertyPaths(MetaClass metaClass) {
        List<MetaPropertyPath> res = new ArrayList<>();
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            res.add(new MetaPropertyPath(metaClass, metaProperty));
        }
        return res;
    }

    /**
     * Converts a collection of properties to collection of {@link MetaPropertyPath}s containing one property each
     */
    public Collection<MetaPropertyPath> toPropertyPaths(Collection<MetaProperty> properties) {
        List<MetaPropertyPath> res = new ArrayList<>();
        for (MetaProperty metaProperty : properties) {
            res.add(new MetaPropertyPath(metaProperty.getDomain(), metaProperty));
        }
        return res;
    }

    /**
     * Collects {@link MetaPropertyPath}s defined by the given view, traversing the whole view graph.
     * @param view      starting view
     * @param metaClass metaclass for which the view was defined
     * @return collection of paths
     */
    public Collection<MetaPropertyPath> getViewPropertyPaths(View view, MetaClass metaClass) {
        List<MetaPropertyPath> propertyPaths = new ArrayList<>(metaClass.getProperties().size());
        for (final MetaProperty metaProperty : metaClass.getProperties()) {
            final MetaPropertyPath metaPropertyPath = new MetaPropertyPath(metaClass, metaProperty);
            if (viewContainsProperty(view, metaPropertyPath)) {
                propertyPaths.add(metaPropertyPath);
            }
        }
        return propertyPaths;
    }

    /**
     * Determine whether the view contains a property, traversing the view graph according to the given property path.
     * @param view         view instance. If null, return false immediately.
     * @param propertyPath property path defining the property
     */
    public boolean viewContainsProperty(@Nullable View view, MetaPropertyPath propertyPath) {
        View currentView = view;
        for (MetaProperty metaProperty : propertyPath.getMetaProperties()) {
            if (currentView == null)
                return false;

            ViewProperty property = currentView.getProperty(metaProperty.getName());
            if (property == null)
                return false;

            currentView = property.getView();
        }
        return true;
    }

    /**
     * @return collection of all persistent entities
     */
    public Collection<MetaClass> getAllPersistentMetaClasses() {
        Set<MetaClass> result = new LinkedHashSet<>();
        for (MetaClass metaClass : metadata.getSession().getClasses()) {
            if (isPersistent(metaClass)) {
                result.add(metaClass);
            }
        }
        return result;
    }

    /**
     * @return collection of all embeddable entities
     */
    public Collection<MetaClass> getAllEmbeddableMetaClasses() {
        List<MetaClass> result = new ArrayList<>();
        for (MetaClass metaClass : metadata.getSession().getClasses()) {
            if (metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Embeddable.class)) {
                result.add(metaClass);
            }
        }
        return result;
    }

    /**
     * @return collection of all Java enums used as a type of an entity attribute
     */
    public Collection<Class> getAllEnums() {
        if (enums == null) {
            synchronized (this) {
                enums = new HashSet<>();
                for (MetaClass metaClass : metadata.getSession().getClasses()) {
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

    /**
     * @return table name for the given entity, or null if the entity is Embeddable, MappedSuperclass or non-persistent
     */
    @Nullable
    public String getDatabaseTable(MetaClass metaClass) {
        if (isEmbeddable(metaClass) || !isPersistent(metaClass))
            return null;

        Class<?> javaClass = metaClass.getJavaClass();
        javax.persistence.Table annotation = javaClass.getAnnotation(javax.persistence.Table.class);
        if (annotation != null && StringUtils.isNotEmpty(annotation.name())) {
            return annotation.name();
        } else if (metaClass.getAncestor() != null) {
            return getDatabaseTable(metaClass.getAncestor());
        }

        return null;
    }

    /**
     * @return list of related properties defined in {@link com.haulmont.chile.core.annotations.MetaProperty#related()}
     * or empty list
     */
    public List<String> getRelatedProperties(Class<?> entityClass, String property) {
        List<String> result = new ArrayList<>();
        MetaClass metaClass = metadata.getClassNN(entityClass);
        return getRelatedProperties(metaClass.getPropertyNN(property));
    }

    /**
     * @return list of related properties defined in {@link com.haulmont.chile.core.annotations.MetaProperty#related()}
     * or empty list
     */
    public List<String> getRelatedProperties(MetaProperty metaProperty) {
        List<String> result = new ArrayList<>();
        String relatedProperties = (String) metaProperty.getAnnotations().get("relatedProperties");
        if (relatedProperties != null) {
            result.addAll(Arrays.asList(relatedProperties.split(",")));
        }
        return result;
    }

    @Nullable
    public MetaPropertyPath resolveMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath metaPropertyPath = metaClass.getPropertyPath(property);
        if (metaPropertyPath == null && DynamicAttributesUtils.isDynamicAttribute(property)) {
            metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(metaClass, property);
        }
        return metaPropertyPath;
    }
}