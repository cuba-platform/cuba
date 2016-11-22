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

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

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
 */
@Component(MetadataTools.NAME)
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

    @Inject
    protected PersistentAttributesLoadChecker persistentAttributesLoadChecker;

    protected volatile Collection<Class> enums;

    /**
     * Default constructor used by container at runtime and in server-side integration tests.
     */
    public MetadataTools() {
    }

    /**
     * Formats a value according to the property type.
     *
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
     *
     * @param value object to format
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
     * @return name of a data store of the given entity or null if the entity is not persistent and no data store
     * is defined for it
     */
    @Nullable
    public String getStoreName(MetaClass metaClass) {
        String storeName = (String) metaClass.getAnnotations().get(Stores.PROP_NAME);
        if (storeName == null) {
            return isPersistent(metaClass) ? Stores.MAIN : null;
        } else
            return storeName;
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
     * @return true if the first MetaClass is equal or an ancestor of the second.
     */
    public boolean isAssignableFrom(MetaClass metaClass, MetaClass other) {
        Preconditions.checkNotNullArgument(metaClass);
        Preconditions.checkNotNullArgument(other);
        return metaClass.equals(other) || metaClass.getDescendants().contains(other);
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
     * {@link Entity}, {@link Creatable}, {@link Updatable}, {@link SoftDelete}, {@link Versioned}
     */
    public boolean isSystem(MetaProperty metaProperty) {
        Objects.requireNonNull(metaProperty, "metaProperty is null");
        return Boolean.TRUE.equals(metaProperty.getAnnotations().get("system"));
    }

    /**
     * Determine whether all the properties defined by the given property path are persistent.
     *
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
     *
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
     *
     * @see Embedded
     */
    public boolean isEmbedded(MetaProperty metaProperty) {
        Objects.requireNonNull(metaProperty, "metaProperty is null");
        return Boolean.TRUE.equals(metaProperty.getAnnotations().get("embedded"));
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
     *
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
     *
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
     * Determine whether the given metaclass represents a persistent entity.
     * Mapped superclasses and Embeddables are not considered persistent.
     */
    public boolean isPersistent(MetaClass metaClass) {
        checkNotNullArgument(metaClass, "metaClass is null");
        return metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Entity.class);
    }

    /**
     * Determine whether the given class represents a persistent entity.
     * Mapped superclasses and Embeddables are not considered persistent.
     */
    public boolean isPersistent(Class aClass) {
        checkNotNullArgument(aClass, "class is null");
        return aClass.isAnnotationPresent(javax.persistence.Entity.class);
    }

    /**
     * Determine whether the given metaclass represents a non-persistent entity.
     */
    public boolean isTransient(MetaClass metaClass) {
        return isTransient(metaClass.getJavaClass());
    }

    /**
     * Determine whether the given class represents a non-persistent entity.
     */
    public boolean isTransient(Class aClass) {
        checkNotNullArgument(aClass, "class is null");
        return AbstractNotPersistentEntity.class.isAssignableFrom(aClass);
    }

    /**
     * Determine whether the given metaclass is embeddable.
     */
    public boolean isEmbeddable(MetaClass metaClass) {
        checkNotNullArgument(metaClass, "metaClass is null");
        return metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Embeddable.class);
    }

    public boolean isCacheable(MetaClass metaClass) {
        checkNotNullArgument(metaClass, "metaClass is null");
        return Boolean.TRUE.equals(metaClass.getAnnotations().get("cacheable"));
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
     *
     * @param metaClass entity metaclass
     * @return collection of the name pattern properties
     */
    @Nonnull
    public Collection<MetaProperty> getNamePatternProperties(MetaClass metaClass) {
        return getNamePatternProperties(metaClass, false);
    }

    /**
     * Return a collection of properties included into entity's name pattern (see {@link NamePattern}).
     *
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
     * Collects all meta-properties of the given meta-class included to the given view as {@link MetaPropertyPath}s.
     *
     * @param view      view
     * @param metaClass meta-class
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
     * Determine whether the view contains a property, traversing a view branch according to the given property path.
     *
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

    @Nullable
    public String getDatabaseColumn(MetaProperty metaProperty) {
        if (!isPersistent(metaProperty))
            return null;
        Column column = metaProperty.getAnnotatedElement().getAnnotation(Column.class);
        if (column != null) {
            return StringUtils.isEmpty(column.name()) ? metaProperty.getName() : column.name();
        }
        JoinColumn joinColumn = metaProperty.getAnnotatedElement().getAnnotation(JoinColumn.class);
        if (joinColumn != null) {
            return StringUtils.isEmpty(joinColumn.name()) ? metaProperty.getName() : joinColumn.name();
        }
        return null;
    }

    /**
     * @return list of related properties defined in {@link com.haulmont.chile.core.annotations.MetaProperty#related()}
     * or empty list
     */
    public List<String> getRelatedProperties(Class<?> entityClass, String property) {
        Preconditions.checkNotNullArgument(entityClass, "entityClass is null");

        MetaClass metaClass = metadata.getClassNN(entityClass);
        return getRelatedProperties(metaClass.getPropertyNN(property));
    }

    /**
     * @return list of related properties defined in {@link com.haulmont.chile.core.annotations.MetaProperty#related()}
     * or empty list
     */
    public List<String> getRelatedProperties(MetaProperty metaProperty) {
        Preconditions.checkNotNullArgument(metaProperty, "metaProperty is null");

        List<String> result = new ArrayList<>();
        String relatedProperties = (String) metaProperty.getAnnotations().get("relatedProperties");
        if (relatedProperties != null) {
            result.addAll(Arrays.asList(relatedProperties.split(",")));
        }
        return result;
    }

    /**
     * Returns a {@link MetaPropertyPath} which can include the special MetaProperty for a dynamic attribute.
     *
     * @param metaClass    originating meta-class
     * @param propertyPath path to the attribute
     * @return MetaPropertyPath instance
     */
    @Nullable
    public MetaPropertyPath resolveMetaPropertyPath(MetaClass metaClass, String propertyPath) {
        Preconditions.checkNotNullArgument(metaClass, "metaClass is null");

        MetaPropertyPath metaPropertyPath = metaClass.getPropertyPath(propertyPath);
        if (metaPropertyPath == null && DynamicAttributesUtils.isDynamicAttribute(propertyPath)) {
            metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(metaClass, propertyPath);
        }
        return metaPropertyPath;
    }

    /**
     * Depth-first traversal of the object graph starting from the specified entity instance.
     * Visits all attributes.
     *
     * @param entity  entity graph entry point
     * @param visitor the attribute visitor implementation
     */
    public void traverseAttributes(Entity entity, EntityAttributeVisitor visitor) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Preconditions.checkNotNullArgument(visitor, "visitor is null");

        internalTraverseAttributes(entity, visitor, new HashSet<>());
    }

    /**
     * Depth-first traversal of the object graph by the view starting from the specified entity instance.
     * Visits attributes defined in the view.
     *
     * @param view    view instance
     * @param entity  entity graph entry point
     * @param visitor the attribute visitor implementation
     */
    public void traverseAttributesByView(View view, Entity entity, EntityAttributeVisitor visitor) {
        Preconditions.checkNotNullArgument(view, "view is null");
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Preconditions.checkNotNullArgument(visitor, "visitor is null");

        internalTraverseAttributesByView(view, entity, visitor, new HashMap<>());
    }

    /**
     * Create a new instance and make it a shallow copy of the instance given.
     * <p/> This method copies attributes according to the metadata and relies on {@link com.haulmont.chile.core.model.Instance#getMetaClass()}
     * method which should not return null.
     *
     * @param source source instance
     * @return new instance of the same Java class as source
     */
    public <T extends Instance> T copy(T source) {
        checkNotNullArgument(source, "source is null");

        //noinspection unchecked
        T dest = createInstance((Class<T>) source.getClass());

        copy(source, dest);
        return dest;
    }

    /**
     * Make a shallow copy of an instance.
     * <p/> This method copies attributes according to the metadata and relies on {@link com.haulmont.chile.core.model.Instance#getMetaClass()}
     * method which should not return null for both objects.
     * <p/> The source and destination instances don't have to be of the same Java class or metaclass. Copying is
     * performed in the following scenario: get each source property and copy the value to the destination if it
     * contains a property with the same name and it is not read-only.
     *
     * @param source source instance
     * @param dest   destination instance
     */
    public void copy(Instance source, Instance dest) {
        checkNotNullArgument(source, "source is null");
        checkNotNullArgument(dest, "dest is null");

        MetaClass sourceMetaClass = metadata.getClassNN(source.getClass());
        MetaClass destMetaClass = metadata.getClassNN(dest.getClass());
        for (MetaProperty srcProperty : sourceMetaClass.getProperties()) {
            String name = srcProperty.getName();
            MetaProperty dstProperty = destMetaClass.getProperty(name);
            if (dstProperty != null && !dstProperty.isReadOnly() && persistentAttributesLoadChecker.isLoaded(source, name)) {
                try {
                    dest.setValue(name, source.getValue(name));
                } catch (RuntimeException e) {
                    Throwable cause = ExceptionUtils.getRootCause(e);
                    if (cause == null)
                        cause = e;
                    // ignore exception on copy for not loaded fields
                    if (!(cause instanceof IllegalStateException))
                        throw e;
                }
            }
        }

        if (source instanceof BaseGenericIdEntity && dest instanceof BaseGenericIdEntity) {
            ((BaseGenericIdEntity) dest).setDynamicAttributes(((BaseGenericIdEntity<?>) source).getDynamicAttributes());
        }
    }

    public interface EntitiesHolder {
        Entity create(Class<? extends Entity> entityClass, Object id);

        Entity find(Object id);

        void put(Entity entity);
    }

    public static class CachingEntitiesHolder implements EntitiesHolder {
        protected Map<Object, Entity> cache = new HashMap<>();

        @Override
        public Entity create(Class<? extends Entity> entityClass, Object id) {
            Entity entity = cache.get(id);
            if (entity == null) {
                entity = createInstanceWithId(entityClass, id);
                cache.put(id, entity);
            }

            return entity;
        }

        @Override
        public Entity find(Object id) {
            return cache.get(id);
        }

        @Override
        public void put(Entity entity) {
            cache.put(entity.getId(), entity);
        }
    }

    /**
     * Make deep copy of the source entity: all referred entities ant collections will be copied as well
     */
    public <T extends Entity> T deepCopy(T source) {
        CachingEntitiesHolder entityFinder = new CachingEntitiesHolder();
        Entity destination = entityFinder.create(source.getClass(), source.getId());
        deepCopy(source, destination, entityFinder);

        return (T) destination;
    }

    /**
     * Copies all property values from source to destination excluding null values.
     */
    public void deepCopy(Entity source, Entity destination, EntitiesHolder entitiesHolder) {
        for (MetaProperty srcProperty : source.getMetaClass().getProperties()) {
            String name = srcProperty.getName();

            if (srcProperty.isReadOnly() || !persistentAttributesLoadChecker.isLoaded(source, name)) {
                continue;
            }

            Object value = source.getValue(name);
            if (value == null) {
                continue;
            }

            if (srcProperty.getRange().isClass()) {
                Class refClass = srcProperty.getRange().asClass().getJavaClass();
                if (!isPersistent(refClass)) {
                    continue;
                }

                if (srcProperty.getRange().getCardinality().isMany()) {
                    //noinspection unchecked
                    Collection<Entity> srcCollection = (Collection) value;
                    Collection<Entity> dstCollection = value instanceof List ? new ArrayList<>() : new LinkedHashSet<>();

                    for (Entity srcRef : srcCollection) {
                        Entity reloadedRef = entitiesHolder.find(srcRef.getId());
                        if (reloadedRef == null) {
                            reloadedRef = entitiesHolder.create(srcRef.getClass(), srcRef.getId());
                            deepCopy(srcRef, reloadedRef, entitiesHolder);
                        }
                        dstCollection.add(reloadedRef);
                    }
                    destination.setValue(name, dstCollection);
                } else {
                    Entity srcRef = (Entity) value;
                    Entity reloadedRef = entitiesHolder.find(srcRef.getId());
                    if (reloadedRef == null) {
                        reloadedRef = entitiesHolder.create(srcRef.getClass(), srcRef.getId());
                        deepCopy(srcRef, reloadedRef, entitiesHolder);
                    }
                    destination.setValue(name, reloadedRef);
                }
            } else {
                destination.setValue(name, value);
            }
        }

        if (source instanceof BaseGenericIdEntity && destination instanceof BaseGenericIdEntity) {
            ((BaseGenericIdEntity) destination).setDynamicAttributes(((BaseGenericIdEntity<?>) source).getDynamicAttributes());
        }
    }

    protected void internalTraverseAttributes(Entity entity, EntityAttributeVisitor visitor, HashSet<Object> visited) {
        if (visited.contains(entity))
            return;
        visited.add(entity);

        for (MetaProperty property : entity.getMetaClass().getProperties()) {
            if (visitor.skip(property))
                continue;

            visitor.visit(entity, property);
            if (property.getRange().isClass()) {
                if (persistentAttributesLoadChecker.isLoaded(entity, property.getName())) {
                    Object value = entity.getValue(property.getName());
                    if (value != null) {
                        if (value instanceof Collection) {
                            for (Object item : ((Collection) value)) {
                                internalTraverseAttributes((Entity) item, visitor, visited);
                            }
                        } else {
                            internalTraverseAttributes((Entity) value, visitor, visited);
                        }
                    }
                }
            }
        }
    }

    protected void internalTraverseAttributesByView(View view, Entity entity, EntityAttributeVisitor visitor,
                                                    Map<Entity, Set<View>> visited) {
        Set<View> views = visited.get(entity);
        if (views == null) {
            views = new HashSet<>();
            visited.put(entity, views);
        } else if (views.contains(view)) {
            return;
        }
        views.add(view);

        MetaClass metaClass = metadata.getClassNN(entity.getClass());

        for (ViewProperty property : view.getProperties()) {
            View propertyView = property.getView();

            visitor.visit(entity, metaClass.getPropertyNN(property.getName()));

            Object value = entity.getValue(property.getName());

            if (value != null && propertyView != null) {
                if (value instanceof Collection) {
                    for (Object item : ((Collection) value)) {
                        if (item instanceof Instance)
                            internalTraverseAttributesByView(propertyView, (Entity) item, visitor, visited);
                    }
                } else if (value instanceof Instance) {
                    internalTraverseAttributesByView(propertyView, (Entity) value, visitor, visited);
                }
            }
        }
    }

    protected static <T> T createInstance(Class<T> aClass) {
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected static Entity createInstanceWithId(Class<? extends Entity> entityClass, Object id) {
        Entity entity = createInstance(entityClass);
        if (entity instanceof BaseGenericIdEntity) {
            ((BaseGenericIdEntity) entity).setId(id);
        }
        return entity;
    }
}