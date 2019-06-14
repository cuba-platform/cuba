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

package com.haulmont.cuba.core.sys;

import com.google.common.base.Joiner;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.impl.AdaptiveNumberDatatype;
import com.haulmont.chile.core.datatypes.impl.EnumerationImpl;
import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.impl.*;
import com.haulmont.cuba.core.entity.annotation.MetaAnnotation;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.validation.groups.UiComponentChecks;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * INTERNAL.
 * Loads meta-model from a set of annotated Java classes.
 */
@Component(MetaModelLoader.NAME)
@Scope("prototype")
public class MetaModelLoader {

    public static final String NAME = "cuba_MetaModelLoader";

    protected static final String VALIDATION_MIN = "_min";
    protected static final String VALIDATION_MAX = "_max";

    protected static final String VALIDATION_NOTNULL_MESSAGE = "_notnull_message";
    protected static final String VALIDATION_NOTNULL_UI_COMPONENT = "_notnull_ui_component";

    protected DatatypeRegistry datatypes;

    protected Session session;

    private static final Logger log = LoggerFactory.getLogger(MetaModelLoader.class);

    public MetaModelLoader(Session session) {
        this.session = session;
    }

    @Inject
    public void setDatatypeRegistry(DatatypeRegistry datatypeRegistry) {
        this.datatypes = datatypeRegistry;
    }

    public void loadModel(String rootPackage, List<EntityClassInfo> classInfos) {
        checkNotNullArgument(rootPackage, "rootPackage is null");
        checkNotNullArgument(classInfos, "classInfos is null");

        Map<Class<?>, Boolean> classes = new LinkedHashMap<>();
        for (EntityClassInfo classInfo : classInfos) {
            try {
                classes.put(ReflectionHelper.loadClass(classInfo.name), classInfo.persistent);
            } catch (ClassNotFoundException e) {
                log.warn("Class {} not found for model {}", classInfo.name, rootPackage);
            }
        }


        for (Map.Entry<Class<?>, Boolean> entry : classes.entrySet()) {
            Class<?> aClass = entry.getKey();
            if (aClass.getName().startsWith(rootPackage)) {
                MetaClassImpl metaClass = createClass(aClass, rootPackage);
                if (metaClass == null) {
                    log.warn("Class {} is not loaded into metadata", aClass.getName());
                }
            } else {
                log.warn("Class {} is not under root package {} and will not be included to metadata", aClass.getName(), rootPackage);
            }
        }

        for (Map.Entry<Class<?>, Boolean> entry : classes.entrySet()) {
            Class<?> aClass = entry.getKey();
            MetaClassImpl metaClass = (MetaClassImpl) session.getClass(aClass);
            if (metaClass != null) {
                onClassLoaded(metaClass, aClass, entry.getValue());
            }
        }

        List<RangeInitTask> tasks = new ArrayList<>();
        for (Map.Entry<Class<?>, Boolean> entry : classes.entrySet()) {
            Class<?> aClass = entry.getKey();
            if (aClass.getName().startsWith(rootPackage)) {
                MetadataObjectInfo<MetaClass> info = loadClass(rootPackage, aClass, entry.getValue());
                if (info != null) {
                    tasks.addAll(info.getTasks());
                } else {
                    log.warn("Class {} is not loaded into metadata", aClass.getName());
                }
            } else {
                log.warn("Class {} is not under root package {} and will not be included to metadata", aClass.getName(), rootPackage);
            }
        }

        for (RangeInitTask task : tasks) {
            task.execute();
        }
    }

    @Nullable
    protected MetadataObjectInfo<MetaClass> loadClass(String packageName, Class<?> javaClass, boolean persistent) {
        MetaClassImpl metaClass = (MetaClassImpl) session.getClass(javaClass);
        if (metaClass == null)
            return null;

        Collection<RangeInitTask> tasks = new ArrayList<>();

        Collection<MetaClass> ancestors = metaClass.getAncestors();
        for (MetaClass ancestor : ancestors) {
            initProperties(ancestor.getJavaClass(), ((MetaClassImpl) ancestor), tasks);
        }

        initProperties(javaClass, metaClass, tasks);

        return new MetadataObjectInfo<>(metaClass, tasks);
    }

    @Nullable
    protected MetaClassImpl createClass(Class<?> javaClass, String packageName) {
        if (AbstractInstance.class.equals(javaClass) || Object.class.equals(javaClass)) {
            return null;
        }

        MetaClassImpl metaClass = (MetaClassImpl) session.getClass(javaClass);
        if (metaClass != null) {
            return metaClass;

        } else if (packageName == null || javaClass.getName().startsWith(packageName)) {
            String name = getMetaClassName(javaClass);
            if (name == null)
                return null;

            metaClass = createClassInModel(packageName, name);
            metaClass.setJavaClass(javaClass);

            Class<?> ancestor = javaClass.getSuperclass();
            if (ancestor != null) {
                MetaClass ancestorClass = createClass(ancestor, packageName);
                if (ancestorClass != null) {
                    metaClass.addAncestor(ancestorClass);
                }
            }

            return metaClass;
        } else {
            return null;
        }
    }

    protected String getMetaClassName(Class<?> javaClass) {
        Entity entityAnnotation = javaClass.getAnnotation(Entity.class);
        MappedSuperclass mappedSuperclassAnnotation = javaClass.getAnnotation(MappedSuperclass.class);

        com.haulmont.chile.core.annotations.MetaClass metaClassAnnotation = javaClass.getAnnotation(com.haulmont.chile.core.annotations.MetaClass.class);
        Embeddable embeddableAnnotation = javaClass.getAnnotation(Embeddable.class);

        if ((entityAnnotation == null && mappedSuperclassAnnotation == null) &&
                (embeddableAnnotation == null) && (metaClassAnnotation == null)) {
            log.trace("Class '{}' isn't annotated as metadata entity, ignore it", javaClass.getName());
            return null;
        }

        String name = null;
        if (entityAnnotation != null) {
            name = entityAnnotation.name();
        } else if (metaClassAnnotation != null) {
            name = metaClassAnnotation.name();
        }

        if (StringUtils.isEmpty(name)) {
            name = javaClass.getSimpleName();
        }
        return name;
    }

    protected void onClassLoaded(MetaClass metaClass, Class<?> javaClass, boolean persistent) {
        if (persistent) {
            metaClass.getAnnotations().put(MetadataTools.PERSISTENT_ANN_NAME, true);
        }
    }

    protected void initProperties(Class<?> clazz, MetaClassImpl metaClass, Collection<RangeInitTask> tasks) {
        if (!metaClass.getOwnProperties().isEmpty())
            return;

        // load collection properties after non-collection in order to have all inverse properties loaded up
        ArrayList<Field> collectionProps = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isSynthetic())
                continue;

            final String fieldName = field.getName();

            if (isMetaPropertyField(field)) {
                MetaPropertyImpl property = (MetaPropertyImpl) metaClass.getProperty(fieldName);
                if (property == null) {
                    MetadataObjectInfo<MetaProperty> info;
                    if (isCollection(field) || isMap(field)) {
                        collectionProps.add(field);
                    } else {
                        info = loadProperty(metaClass, field);
                        tasks.addAll(info.getTasks());
                        MetaProperty metaProperty = info.getObject();
                        onPropertyLoaded(metaProperty, field);
                    }
                } else {
                    log.warn("Field " + clazz.getSimpleName() + "." + field.getName()
                            + " is not included in metadata because property " + property + " already exists");
                }
            }
        }

        for (Field f : collectionProps) {
            MetadataObjectInfo<MetaProperty> info = loadCollectionProperty(metaClass, f);
            tasks.addAll(info.getTasks());
            MetaProperty metaProperty = info.getObject();
            onPropertyLoaded(metaProperty, f);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isSynthetic())
                continue;

            String methodName = method.getName();
            if (!methodName.startsWith("get") || method.getReturnType() == void.class)
                continue;

            if (isMetaPropertyMethod(method)) {
                String name = StringUtils.uncapitalize(methodName.substring(3));

                MetaPropertyImpl property = (MetaPropertyImpl) metaClass.getProperty(name);
                if (property == null) {
                    MetadataObjectInfo<MetaProperty> info;
                    if (isCollection(method) || isMap(method)) {
                        throw new UnsupportedOperationException(String.format("Method-based property %s.%s doesn't support collections and maps", clazz.getSimpleName(), method.getName()));
                    } else if (method.getParameterCount() != 0) {
                        throw new UnsupportedOperationException(String.format("Method-based property %s.%s doesn't support arguments", clazz.getSimpleName(), method.getName()));
                    } else {
                        info = loadProperty(metaClass, method, name);
                        tasks.addAll(info.getTasks());
                    }
                    MetaProperty metaProperty = info.getObject();
                    onPropertyLoaded(metaProperty, method);
                } else {
                    log.warn("Method " + clazz.getSimpleName() + "." + method.getName()
                            + " is not included in metadata because property " + property + " already exists");
                }
            }
        }
    }

    protected boolean isMetaPropertyField(Field field) {
        return field.isAnnotationPresent(Column.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToMany.class)
                || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(Embedded.class)
                || field.isAnnotationPresent(EmbeddedId.class)
                || field.isAnnotationPresent(com.haulmont.chile.core.annotations.MetaProperty.class);
    }

    protected boolean isMetaPropertyMethod(Method method) {
        return method.isAnnotationPresent(com.haulmont.chile.core.annotations.MetaProperty.class);
    }

    protected MetadataObjectInfo<MetaProperty> loadProperty(MetaClassImpl metaClass, Field field) {
        Collection<RangeInitTask> tasks = new ArrayList<>();

        MetaPropertyImpl property = new MetaPropertyImpl(metaClass, field.getName());

        Range.Cardinality cardinality = getCardinality(field);
        Map<String, Object> map = new HashMap<>();
        map.put("cardinality", cardinality);
        boolean mandatory = isMandatory(field);
        map.put("mandatory", mandatory);
        Datatype datatype = getAdaptiveDatatype(field);
        map.put("datatype", datatype);
        String inverseField = getInverseField(field);
        if (inverseField != null)
            map.put("inverseField", inverseField);

        Class<?> type;
        Class typeOverride = getTypeOverride(field);
        if (typeOverride != null)
            type = typeOverride;
        else
            type = field.getType();

        property.setMandatory(mandatory);
        property.setReadOnly(!setterExists(field));
        property.setAnnotatedElement(field);
        property.setDeclaringClass(field.getDeclaringClass());

        MetadataObjectInfo<Range> info = loadRange(property, type, map);
        Range range = info.getObject();
        if (range != null) {
            ((AbstractRange) range).setCardinality(cardinality);
            property.setRange(range);
            assignPropertyType(field, property, range);
            assignInverse(property, range, inverseField);
        }

        if (info.getObject() != null && info.getObject().isEnum()) {
            property.setJavaType(info.getObject().asEnumeration().getJavaClass());
        } else {
            property.setJavaType(field.getType());
        }

        tasks.addAll(info.getTasks());

        return new MetadataObjectInfo<>(property, tasks);
    }

    protected MetadataObjectInfo<MetaProperty> loadProperty(MetaClassImpl metaClass,
                                                            Method method, String name) {
        Collection<RangeInitTask> tasks = new ArrayList<>();

        MetaPropertyImpl property = new MetaPropertyImpl(metaClass, name);

        Map<String, Object> map = new HashMap<>();
        map.put("cardinality", Range.Cardinality.NONE);
        map.put("mandatory", false);
        Datatype datatype = getAdaptiveDatatype(method);
        map.put("datatype", datatype);

        Class<?> type;
        Class typeOverride = getTypeOverride(method);
        if (typeOverride != null)
            type = typeOverride;
        else
            type = method.getReturnType();

        property.setMandatory(false);
        property.setReadOnly(!setterExists(method));
        property.setAnnotatedElement(method);
        property.setDeclaringClass(method.getDeclaringClass());
        property.setJavaType(method.getReturnType());

        MetadataObjectInfo<Range> info = loadRange(property, type, map);
        Range range = info.getObject();
        if (range != null) {
            ((AbstractRange) range).setCardinality(Range.Cardinality.NONE);
            property.setRange(range);
            assignPropertyType(method, property, range);
        }

        tasks.addAll(info.getTasks());

        return new MetadataObjectInfo<>(property, tasks);
    }

    protected MetadataObjectInfo<MetaProperty> loadCollectionProperty(MetaClassImpl metaClass, Field field) {
        Collection<RangeInitTask> tasks = new ArrayList<>();

        MetaPropertyImpl property = new MetaPropertyImpl(metaClass, field.getName());

        Class type = getFieldType(field);

        Range.Cardinality cardinality = getCardinality(field);
        boolean ordered = isOrdered(field);
        boolean mandatory = isMandatory(field);
        String inverseField = getInverseField(field);

        Map<String, Object> map = new HashMap<>();
        map.put("cardinality", cardinality);
        map.put("ordered", ordered);
        map.put("mandatory", mandatory);
        if (inverseField != null)
            map.put("inverseField", inverseField);

        property.setAnnotatedElement(field);
        property.setDeclaringClass(field.getDeclaringClass());
        property.setJavaType(field.getType());

        MetadataObjectInfo<Range> info = loadRange(property, type, map);
        Range range = info.getObject();
        if (range != null) {
            ((AbstractRange) range).setCardinality(cardinality);
            ((AbstractRange) range).setOrdered(ordered);
            property.setRange(range);
            assignPropertyType(field, property, range);
            assignInverse(property, range, inverseField);
        }
        property.setMandatory(mandatory);

        tasks.addAll(info.getTasks());

        return new MetadataObjectInfo<>(property, tasks);
    }

    protected void onPropertyLoaded(MetaProperty metaProperty, Field field) {
        loadPropertyAnnotations(metaProperty, field);

        boolean persistentClass = Boolean.TRUE.equals(metaProperty.getDomain().getAnnotations().get(MetadataTools.PERSISTENT_ANN_NAME));

        if (isPrimaryKey(field)) {
            metaProperty.getAnnotations().put(MetadataTools.PRIMARY_KEY_ANN_NAME, true);
            metaProperty.getDomain().getAnnotations().put(MetadataTools.PRIMARY_KEY_ANN_NAME, metaProperty.getName());
        }

        if (isPersistent(field) && persistentClass) {
            metaProperty.getAnnotations().put(MetadataTools.PERSISTENT_ANN_NAME, true);
        }

        if (isEmbedded(field) && persistentClass) {
            metaProperty.getAnnotations().put(MetadataTools.EMBEDDED_ANN_NAME, true);
        }

        Column column = field.getAnnotation(Column.class);
        Lob lob = field.getAnnotation(Lob.class);
        if (column != null && column.length() != 0 && lob == null) {
            metaProperty.getAnnotations().put("length", column.length());
        }

        Temporal temporal = field.getAnnotation(Temporal.class);
        if (temporal != null) {
            metaProperty.getAnnotations().put(MetadataTools.TEMPORAL_ANN_NAME, temporal.value());
        }

        boolean system = isPrimaryKey(field) || propertyBelongsTo(field, metaProperty, MetadataTools.SYSTEM_INTERFACES);
        if (system)
            metaProperty.getAnnotations().put(MetadataTools.SYSTEM_ANN_NAME, true);
    }

    private boolean propertyBelongsTo(Field field, MetaProperty metaProperty, List<Class> systemInterfaces) {
        String getterName = "get" + StringUtils.capitalize(metaProperty.getName());

        Class<?> aClass = field.getDeclaringClass();
        //noinspection unchecked
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(aClass);
        for (Class intf : allInterfaces) {
            Method[] methods = intf.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(getterName) && method.getParameterTypes().length == 0) {
                    if (systemInterfaces.contains(intf))
                        return true;
                }
            }
        }
        return false;
    }

    protected Class getFieldTypeAccordingAnnotations(Field field) {
        OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        ManyToOne manyToOneAnnotation = field.getAnnotation(ManyToOne.class);
        ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);

        Class result = null;
        if (oneToOneAnnotation != null) {
            result = oneToOneAnnotation.targetEntity();
        } else if (oneToManyAnnotation != null) {
            result = oneToManyAnnotation.targetEntity();
        } else if (manyToOneAnnotation != null) {
            result = manyToOneAnnotation.targetEntity();
        } else if (manyToManyAnnotation != null) {
            result = manyToManyAnnotation.targetEntity();
        }
        return result;
    }

    protected Class getTypeOverride(AnnotatedElement element) {
        Temporal temporal = element.getAnnotation(Temporal.class);
        if (temporal != null && temporal.value().equals(TemporalType.DATE))
            return java.sql.Date.class;
        else if (temporal != null && temporal.value().equals(TemporalType.TIME))
            return java.sql.Time.class;
        else
            return null;
    }

    protected boolean isMandatory(Field field) {
        OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);

        if (oneToManyAnnotation != null || manyToManyAnnotation != null) {
            return false;
        }

        Column columnAnnotation = field.getAnnotation(Column.class);
        OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        ManyToOne manyToOneAnnotation = field.getAnnotation(ManyToOne.class);

        com.haulmont.chile.core.annotations.MetaProperty metaPropertyAnnotation =
                field.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);

        boolean superMandatory = (metaPropertyAnnotation != null && metaPropertyAnnotation.mandatory())
                || (field.getAnnotation(NotNull.class) != null
                    && isDefinedForDefaultValidationGroup(field.getAnnotation(NotNull.class)));  // @NotNull without groups

        return (columnAnnotation != null && !columnAnnotation.nullable())
                || (oneToOneAnnotation != null && !oneToOneAnnotation.optional())
                || (manyToOneAnnotation != null && !manyToOneAnnotation.optional())
                || superMandatory;
    }

    protected Range.Cardinality getCardinality(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return Range.Cardinality.NONE;
        } else if (field.isAnnotationPresent(OneToOne.class)) {
            return Range.Cardinality.ONE_TO_ONE;
        } else if (field.isAnnotationPresent(OneToMany.class)) {
            return Range.Cardinality.ONE_TO_MANY;
        } else if (field.isAnnotationPresent(ManyToOne.class)) {
            return Range.Cardinality.MANY_TO_ONE;
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            return Range.Cardinality.MANY_TO_MANY;
        } else if (field.isAnnotationPresent(Embedded.class)) {
            return Range.Cardinality.ONE_TO_ONE;
        } else {
            Class<?> type = field.getType();
            if (Collection.class.isAssignableFrom(type)) {
                return Range.Cardinality.ONE_TO_MANY;
            } else if (type.isPrimitive()
                    || type.equals(String.class)
                    || Number.class.isAssignableFrom(type)
                    || Date.class.isAssignableFrom(type)
                    || UUID.class.isAssignableFrom(type)) {
                return Range.Cardinality.NONE;
            } else
                return Range.Cardinality.MANY_TO_ONE;
        }
    }

    protected String getInverseField(Field field) {
        OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        if (oneToManyAnnotation != null)
            return isBlank(oneToManyAnnotation.mappedBy()) ? null : oneToManyAnnotation.mappedBy();

        ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);
        if (manyToManyAnnotation != null)
            return isBlank(manyToManyAnnotation.mappedBy()) ? null : manyToManyAnnotation.mappedBy();

        OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        if (oneToOneAnnotation != null)
            return isBlank(oneToOneAnnotation.mappedBy()) ? null : oneToOneAnnotation.mappedBy();

        return null;
    }

    protected boolean isPrimaryKey(Field field) {
        return field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class);
    }

    protected boolean isEmbedded(Field field) {
        return field.isAnnotationPresent(Embedded.class) || field.isAnnotationPresent(EmbeddedId.class);
    }

    protected boolean isPersistent(Field field) {
        return field.isAnnotationPresent(Id.class)
                || field.isAnnotationPresent(Column.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToMany.class)
                || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(Embedded.class)
                || field.isAnnotationPresent(EmbeddedId.class);
    }

    protected MetaClassImpl createClassInModel(String modelName, String className) {
        MetaModel model = session.getModel(modelName);
        if (model == null) {
            model = new MetaModelImpl(session, modelName);
        }
        return new MetaClassImpl(model, className);
    }

    protected boolean isCollection(Field field) {
        final Class<?> type = field.getType();
        return Collection.class.isAssignableFrom(type);
    }

    protected boolean isMap(Field field) {
        final Class<?> type = field.getType();
        return Map.class.isAssignableFrom(type);
    }

    protected boolean isMap(Method method) {
        final Class<?> type = method.getReturnType();
        return Map.class.isAssignableFrom(type);
    }

    protected boolean isCollection(Method method) {
        final Class<?> type = method.getReturnType();
        return Collection.class.isAssignableFrom(type);
    }

    protected void onPropertyLoaded(MetaProperty metaProperty, Method method) {
        loadPropertyAnnotations(metaProperty, method);
    }

    protected void loadPropertyAnnotations(MetaProperty metaProperty, AnnotatedElement annotatedElement) {
        for (Annotation annotation : annotatedElement.getAnnotations()) {
            MetaAnnotation metaAnnotation = AnnotationUtils.findAnnotation(annotation.getClass(), MetaAnnotation.class);
            if (metaAnnotation != null) {
                Map<String, Object> attributes = new LinkedHashMap<>(AnnotationUtils.getAnnotationAttributes(annotatedElement, annotation));
                metaProperty.getAnnotations().put(annotation.annotationType().getName(), attributes);
            }
        }

        com.haulmont.chile.core.annotations.MetaProperty metaPropertyAnnotation =
                annotatedElement.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);
        if (metaPropertyAnnotation != null) {
            String[] related = metaPropertyAnnotation.related();
            if (!(related.length == 1 && related[0].equals(""))) {
                metaProperty.getAnnotations().put("relatedProperties", Joiner.on(',').join(related));
            }
        }

        loadBeanValidationAnnotations(metaProperty, annotatedElement);
    }

    protected void loadBeanValidationAnnotations(MetaProperty metaProperty, AnnotatedElement annotatedElement) {
        NotNull notNull = annotatedElement.getAnnotation(NotNull.class);
        if (notNull != null) {
            if (isDefinedForDefaultValidationGroup(notNull)) {
                metaProperty.getAnnotations().put(NotNull.class.getName() + VALIDATION_NOTNULL_MESSAGE, notNull.message());
            }
            if (isDefinedForValidationGroup(notNull, UiComponentChecks.class, true)) {
                metaProperty.getAnnotations().put(NotNull.class.getName() + VALIDATION_NOTNULL_MESSAGE, notNull.message());
                metaProperty.getAnnotations().put(NotNull.class.getName() + VALIDATION_NOTNULL_UI_COMPONENT, true);
            }
        }

        Size size = annotatedElement.getAnnotation(Size.class);
        if (size != null && isDefinedForDefaultValidationGroup(size)) {
            metaProperty.getAnnotations().put(Size.class.getName() + VALIDATION_MIN, size.min());
            metaProperty.getAnnotations().put(Size.class.getName() + VALIDATION_MAX, size.max());
        }

        Length length = annotatedElement.getAnnotation(Length.class);
        if (length != null && isDefinedForDefaultValidationGroup(length)) {
            metaProperty.getAnnotations().put(Length.class.getName() + VALIDATION_MIN, length.min());
            metaProperty.getAnnotations().put(Length.class.getName() + VALIDATION_MAX, length.max());
        }

        Min min = annotatedElement.getAnnotation(Min.class);
        if (min != null && isDefinedForDefaultValidationGroup(min)) {
            metaProperty.getAnnotations().put(Min.class.getName(), min.value());
        }

        Max max = annotatedElement.getAnnotation(Max.class);
        if (max != null && isDefinedForDefaultValidationGroup(max)) {
            metaProperty.getAnnotations().put(Max.class.getName(), max.value());
        }

        DecimalMin decimalMin = annotatedElement.getAnnotation(DecimalMin.class);
        if (decimalMin != null && isDefinedForDefaultValidationGroup(decimalMin)) {
            metaProperty.getAnnotations().put(DecimalMin.class.getName(), decimalMin.value());
        }

        DecimalMax decimalMax = annotatedElement.getAnnotation(DecimalMax.class);
        if (decimalMax != null && isDefinedForDefaultValidationGroup(decimalMax)) {
            metaProperty.getAnnotations().put(DecimalMax.class.getName(), decimalMax.value());
        }

        Past past = annotatedElement.getAnnotation(Past.class);
        if (past != null && isDefinedForDefaultValidationGroup(past)) {
            metaProperty.getAnnotations().put(Past.class.getName(), true);
        }

        Future future = annotatedElement.getAnnotation(Future.class);
        if (future != null && isDefinedForDefaultValidationGroup(future)) {
            metaProperty.getAnnotations().put(Future.class.getName(), true);
        }

        PastOrPresent pastOrPresent = annotatedElement.getAnnotation(PastOrPresent.class);
        if (pastOrPresent != null && isDefinedForDefaultValidationGroup(pastOrPresent)) {
            metaProperty.getAnnotations().put(PastOrPresent.class.getName(), true);
        }

        FutureOrPresent futureOrPresent = annotatedElement.getAnnotation(FutureOrPresent.class);
        if (futureOrPresent != null && isDefinedForDefaultValidationGroup(futureOrPresent)) {
            metaProperty.getAnnotations().put(FutureOrPresent.class.getName(), true);
        }
    }

    protected boolean isDefinedForDefaultValidationGroup(Annotation annotation) {
        return isDefinedForValidationGroup(annotation, javax.validation.groups.Default.class, true);
    }

    protected boolean isDefinedForValidationGroup(Annotation annotation, Class groupClass, boolean inheritDefault) {
        try {
            Method groupsMethod = annotation.getClass().getMethod("groups");
            Class<?>[] groups = (Class<?>[]) groupsMethod.invoke(annotation);
            if (inheritDefault && groups.length == 0) {
                return true;
            }
            return ArrayUtils.contains(groups, groupClass);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Unable to use annotation metadata " + annotation);
        }
    }

    @Nullable
    protected Datatype getAdaptiveDatatype(AnnotatedElement annotatedElement) {
        com.haulmont.chile.core.annotations.MetaProperty annotation =
                annotatedElement.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);
        return annotation != null && !annotation.datatype().equals("") ? datatypes.get(annotation.datatype()) : null;
    }

    protected boolean setterExists(Field field) {
        String name = "set" + StringUtils.capitalize(field.getName());
        Method[] methods = field.getDeclaringClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(name))
                return true;
        }
        return false;
    }

    protected boolean setterExists(Method getter) {
        if (getter.getName().startsWith("get")) {
            String setterName = "set" + getter.getName().substring(3);
            Method[] methods = getter.getDeclaringClass().getDeclaredMethods();
            for (Method method : methods) {
                if (setterName.equals(method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void assignPropertyType(AnnotatedElement field, MetaProperty property, Range range) {
        if (range.isClass()) {
            Composition composition = field.getAnnotation(Composition.class);
            if (composition != null) {
                ((MetaPropertyImpl) property).setType(MetaProperty.Type.COMPOSITION);
            } else {
                ((MetaPropertyImpl) property).setType(MetaProperty.Type.ASSOCIATION);
            }
        } else if (range.isDatatype()) {
            ((MetaPropertyImpl) property).setType(MetaProperty.Type.DATATYPE);
        } else if (range.isEnum()) {
            ((MetaPropertyImpl) property).setType(MetaProperty.Type.ENUM);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("unchecked")
    protected MetadataObjectInfo<Range> loadRange(MetaProperty metaProperty, Class<?> type, Map<String, Object> map) {
        Datatype datatype = (Datatype) map.get("datatype");
        if (datatype != null) {
            // A datatype is assigned explicitly
            return new MetadataObjectInfo<>(new DatatypeRange(datatype));
        }

        datatype = getAdaptiveDatatype(metaProperty, type);
        if (datatype == null) {
            datatype = datatypes.get(type);
        }
        if (datatype != null) {
            MetaClass metaClass = metaProperty.getDomain();
            Class<?> javaClass = metaClass.getJavaClass();

            try {
                String name = "get" + StringUtils.capitalize(metaProperty.getName());
                Method method = javaClass.getMethod(name);

                Class<Enum> returnType = (Class<Enum>) method.getReturnType();
                if (returnType.isEnum()) {
                    return new MetadataObjectInfo<>(new EnumerationRange(new EnumerationImpl<>(returnType)));
                }
            } catch (NoSuchMethodException e) {
                // ignore
            }
            return new MetadataObjectInfo<>(new DatatypeRange(datatype));

        } else if (type.isEnum()) {
            return new MetadataObjectInfo<>(new EnumerationRange(new EnumerationImpl(type)));

        } else {
            return new MetadataObjectInfo<>(null, Collections.singletonList(new RangeInitTask(metaProperty, type, map)));
        }
    }

    @Nullable
    protected Datatype getAdaptiveDatatype(MetaProperty metaProperty, Class<?> type) {
        NumberFormat numberFormat = metaProperty.getAnnotatedElement().getAnnotation(NumberFormat.class);
        if (numberFormat != null) {
            if (Number.class.isAssignableFrom(type)) {
                return new AdaptiveNumberDatatype(type, numberFormat);
            } else {
                log.warn("NumberFormat annotation is ignored because " + metaProperty + " is not a Number");
            }
        }
        return null;
    }

    protected Class getFieldType(Field field) {
        Type genericType = field.getGenericType();
        Class type;
        if (genericType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
            if (Map.class.isAssignableFrom(field.getType()))
                type = (Class<?>) types[1];
            else
                type = (Class<?>) types[0];
        } else {
            type = getFieldTypeAccordingAnnotations(field);
        }
        if (type == null)
            throw new IllegalArgumentException("Field " + field
                    + " must either be of parametrized type or have a JPA annotation declaring a targetEntity");
        return type;
    }

    protected void assignInverse(MetaPropertyImpl property, Range range, String inverseField) {
        if (inverseField == null)
            return;

        if (!range.isClass())
            throw new IllegalArgumentException("Range of class type expected");

        MetaClass metaClass = range.asClass();
        MetaProperty inverseProp = metaClass.getProperty(inverseField);
        if (inverseProp == null)
            throw new RuntimeException(String.format(
                    "Unable to assign inverse property '%s' for '%s'", inverseField, property));
        property.setInverse(inverseProp);
    }

    protected boolean isOrdered(Field field) {
        Class<?> type = field.getType();
        return List.class.isAssignableFrom(type) || LinkedHashSet.class.isAssignableFrom(type);
    }

    protected class RangeInitTask {

        private MetaProperty metaProperty;
        private Class rangeClass;
        private Map<String, Object> map;

        public RangeInitTask(MetaProperty metaProperty, Class rangeClass, Map<String, Object> map) {
            this.metaProperty = metaProperty;
            this.rangeClass = rangeClass;
            this.map = map;
        }

        public String getWarning() {
            return String.format(
                    "Range for property '%s' wasn't initialized (range class '%s')",
                    metaProperty.getName(), rangeClass.getName());
        }

        public void execute() {
            MetaClass rangeClass = session.getClass(this.rangeClass);
            if (rangeClass == null) {
                throw new IllegalStateException(
                        String.format("Can't find range class '%s' for property '%s.%s'",
                                this.rangeClass.getName(), metaProperty.getDomain(), metaProperty.getName()));
            } else {
                ClassRange range = new ClassRange(rangeClass);

                Range.Cardinality cardinality = (Range.Cardinality) map.get("cardinality");
                range.setCardinality(cardinality);
                if (Range.Cardinality.ONE_TO_MANY.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_MANY.equals(cardinality)) {
                    range.setOrdered((Boolean) map.get("ordered"));
                }

                Boolean mandatory = (Boolean) map.get("mandatory");
                if (mandatory != null) {
                    ((MetaPropertyImpl) metaProperty).setMandatory(mandatory);
                }

                ((MetaPropertyImpl) metaProperty).setRange(range);
                assignPropertyType(metaProperty.getAnnotatedElement(), metaProperty, range);

                assignInverse((MetaPropertyImpl) metaProperty, range, (String) map.get("inverseField"));
            }
        }
    }

    public static class MetadataObjectInfo<T> {

        private T object;
        private Collection<RangeInitTask> tasks;

        public MetadataObjectInfo(T object) {
            this.object = object;
            this.tasks = Collections.emptyList();
        }

        public MetadataObjectInfo(T object, Collection<? extends RangeInitTask> tasks) {
            this.object = object;
            this.tasks = (Collection<RangeInitTask>) tasks;
        }

        public T getObject() {
            return object;
        }

        public Collection<RangeInitTask> getTasks() {
            return tasks;
        }

        public void setTasks(Collection<RangeInitTask> tasks) {
            this.tasks = tasks;
        }
    }
}