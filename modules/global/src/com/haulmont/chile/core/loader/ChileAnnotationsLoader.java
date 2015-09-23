/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.loader;

import com.google.common.base.Joiner;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.EnumerationImpl;
import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.impl.*;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class ChileAnnotationsLoader implements MetaClassLoader {

    private Logger log = LoggerFactory.getLogger(ChileAnnotationsLoader.class);

    protected Session session;

    protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    public ChileAnnotationsLoader(Session session) {
        this.session = session;
    }

    @Override
    public void loadPackage(String packageName, List<String> classNames) {
        List<Class<?>> classes;
        List<MetadataObjectInitTask> tasks = new ArrayList<>();

        if (classNames != null) {
            classes = new ArrayList<>();
            for (String className : classNames) {
                try {
                    classes.add(ReflectionHelper.loadClass(className));
                } catch (ClassNotFoundException e) {
                    log.warn("Class " + className + " not found for model " + packageName);
                }
            }
        } else {
            String packagePrefix = packageName.replace(".", "/") + "/**/*.class";
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packagePrefix;
            Resource[] resources;
            try {
                resources = resourcePatternResolver.getResources(packageSearchPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            classes = getClasses(resources);
        }

        for (Class<?> aClass : classes) {
            if (aClass.getName().startsWith(packageName)) {
                MetadataObjectInfo<MetaClass> info = loadClass(packageName, aClass);
                if (info != null) {
                    tasks.addAll(info.getTasks());
                } else if (classNames != null) {
                    // If the class name is specified explicitly, print a warning
                    log.warn("Class " + aClass.getName() + " is not loaded into metadata");
                }
            }
        }

        for (MetadataObjectInitTask task : tasks) {
            task.execute();
        }
    }

    protected List<Class<?>> getClasses(Resource[] resources) {
        List<Class<?>> annotated = new ArrayList<>();

        for (Resource resource : resources) {
            if (resource.isReadable()) {
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                    if (annotationMetadata.isAnnotated(com.haulmont.chile.core.annotations.MetaClass.class.getName())) {
                        ClassMetadata classMetadata = metadataReader.getClassMetadata();
                        Class c = ReflectionHelper.getClass(classMetadata.getClassName());
                        annotated.add(c);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        return annotated;
    }

    protected Class getTypeOverride(AnnotatedElement element) {
        return null;
    }

    protected boolean isMandatory(Field field) {
        com.haulmont.chile.core.annotations.MetaProperty metaPropertyAnnotation =
                field.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);
        return metaPropertyAnnotation != null && metaPropertyAnnotation.mandatory();
    }

    protected boolean isMetaPropertyField(Field field) {
        return field.isAnnotationPresent(com.haulmont.chile.core.annotations.MetaProperty.class);
    }

    protected boolean isMetaPropertyMethod(Method method) {
        return method.isAnnotationPresent(com.haulmont.chile.core.annotations.MetaProperty.class);
    }

    protected MetaClassImpl createMetaClass(String packageName, String className) {
        MetaModel model = session.getModel(packageName);
        if (model == null) {
            model = new MetaModelImpl(session, packageName);
        }
        return new MetaClassImpl(model, className);
    }

    @Nullable
    protected MetaClassImpl createClass(Class<?> clazz, String packageName) {
        if (Object.class.equals(clazz))
            return null;

        final com.haulmont.chile.core.annotations.MetaClass metaClassAnnotaion =
                clazz.getAnnotation(com.haulmont.chile.core.annotations.MetaClass.class);

        if (metaClassAnnotaion == null) {
            log.trace(String.format("Class %s isn't annotated as metadata entity, ignore it", clazz.getName()));
            return null;
        }

        String className = metaClassAnnotaion.name();
        if (StringUtils.isEmpty(className)) {
            className = clazz.getSimpleName();
        }

        return createClass(clazz, packageName, className);
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

    @Nullable
    protected MetadataObjectInfo<MetaClass> loadClass(String packageName, Class<?> clazz) {
        final MetaClassImpl metaClass = createClass(clazz, packageName);
        if (metaClass == null)
            return null;

        Collection<MetadataObjectInitTask> tasks = new ArrayList<>();

        Collection<MetaClass> ancestors = metaClass.getAncestors();
        for (MetaClass ancestor : ancestors) {
            initProperties(ancestor.getJavaClass(), ((MetaClassImpl) ancestor), tasks);
        }

        initProperties(clazz, metaClass, tasks);

        return new MetadataObjectInfo<>(metaClass, tasks);
    }

    protected void initProperties(Class<?> clazz, MetaClassImpl metaClass, Collection<MetadataObjectInitTask> tasks) {
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
                        throw new UnsupportedOperationException("Method-based properties don't support collections and maps");
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void onPropertyLoaded(MetaProperty metaProperty, Field field) {
        loadPropertyAnnotations(metaProperty, field);
    }

    protected void onPropertyLoaded(MetaProperty metaProperty, Method method) {
        loadPropertyAnnotations(metaProperty, method);
    }

    private void loadPropertyAnnotations(MetaProperty metaProperty, AnnotatedElement annotatedElement) {
        SystemLevel systemLevel = annotatedElement.getAnnotation(SystemLevel.class);
        if (systemLevel != null) {
            metaProperty.getAnnotations().put(SystemLevel.class.getName(), systemLevel.value());
            metaProperty.getAnnotations().put(SystemLevel.class.getName() + SystemLevel.PROPAGATE, systemLevel.propagate());
        }

        IgnoreUserTimeZone ignoreUserTimeZone = annotatedElement.getAnnotation(IgnoreUserTimeZone.class);
        if (ignoreUserTimeZone != null) {
            metaProperty.getAnnotations().put(IgnoreUserTimeZone.class.getName(), ignoreUserTimeZone.value());
        }

        com.haulmont.chile.core.annotations.MetaProperty metaPropertyAnnotation =
                annotatedElement.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);
        if (metaPropertyAnnotation != null) {
            String[] related = metaPropertyAnnotation.related();
            if (!(related.length == 1 && related[0].equals(""))) {
                metaProperty.getAnnotations().put("relatedProperties", Joiner.on(',').join(related));
            }
        }
    }

    protected void onClassLoaded(MetaClass metaClass, Class<?> clazz) {
    }

    protected MetadataObjectInfo<MetaProperty> loadProperty(MetaClassImpl metaClass, Field field) {
        Collection<MetadataObjectInitTask> tasks = new ArrayList<>();

        MetaPropertyImpl property = new MetaPropertyImpl(metaClass, field.getName());

        Range.Cardinality cardinality = getCardinality(field);
        Map<String, Object> map = new HashMap<>();
        map.put("cardinality", cardinality);
        boolean mandatory = isMandatory(field);
        map.put("mandatory", mandatory);
        Datatype datatype = getDatatype(field);
        map.put("datatype", datatype);

        Class<?> type;
        Class typeOverride = getTypeOverride(field);
        if (typeOverride != null)
            type = typeOverride;
        else
            type = field.getType();

        MetadataObjectInfo<Range> info = loadRange(property, type, map);
        Range range = info.getObject();
        if (range != null) {
            ((AbstractRange) range).setCardinality(cardinality);
            property.setRange(range);
            assignPropertyType(field, property, range);
        }
        property.setMandatory(mandatory);
        property.setReadOnly(!setterExists(field));
        property.setAnnotatedElement(field);
        property.setDeclaringClass(field.getDeclaringClass());

        if (info.getObject() != null && info.getObject().isEnum()) {
            property.setJavaType(info.getObject().asEnumeration().getJavaClass());
        } else {
            property.setJavaType(field.getType());
        }

        tasks.addAll(info.getTasks());

        return new MetadataObjectInfo<>(property, tasks);
    }

    @Nullable
    protected Datatype getDatatype(AnnotatedElement annotatedElement) {
        com.haulmont.chile.core.annotations.MetaProperty annotation =
                annotatedElement.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);
        return annotation != null && !annotation.datatype().equals("") ? Datatypes.get(annotation.datatype()) : null;
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

    protected MetadataObjectInfo<MetaProperty> loadProperty(MetaClassImpl metaClass,
                                                            Method method, String name) {
        Collection<MetadataObjectInitTask> tasks = new ArrayList<>();

        MetaPropertyImpl property = new MetaPropertyImpl(metaClass, name);

        Map<String, Object> map = new HashMap<>();
        map.put("cardinality", Range.Cardinality.NONE);
        map.put("mandatory", false);
        Datatype datatype = getDatatype(method);
        map.put("datatype", datatype);

        Class<?> type;
        Class typeOverride = getTypeOverride(method);
        if (typeOverride != null)
            type = typeOverride;
        else
            type = method.getReturnType();

        MetadataObjectInfo<Range> info = loadRange(property, type, map);
        Range range = info.getObject();
        if (range != null) {
            ((AbstractRange) range).setCardinality(Range.Cardinality.NONE);
            property.setRange(range);
            assignPropertyType(method, property, range);
        }
        property.setMandatory(false);
        property.setReadOnly(!setterExists(method));
        property.setAnnotatedElement(method);
        property.setDeclaringClass(method.getDeclaringClass());
        property.setJavaType(method.getReturnType());

        tasks.addAll(info.getTasks());

        return new MetadataObjectInfo<>(property, tasks);
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

    @SuppressWarnings({"unchecked"})
    protected MetadataObjectInfo<Range> loadRange(MetaProperty metaProperty, Class type, Map<String, Object> map) {
        Datatype datatype = (Datatype) map.get("datatype");
        if (datatype != null) {
            return new MetadataObjectInfo<>(new DatatypeRange(datatype));
        }

        datatype = Datatypes.get(type);
        if (datatype != null) {
            MetaClass metaClass = metaProperty.getDomain();
            Class javaClass = metaClass.getJavaClass();

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
            return new MetadataObjectInfo<>(new EnumerationRange(new EnumerationImpl<>(type)));

        } else {
            MetaClassImpl rangeClass = (MetaClassImpl) session.getClass(type);
            if (rangeClass != null) {
                return new MetadataObjectInfo<>(new ClassRange(rangeClass));
            } else {
                return new MetadataObjectInfo<>(null, Collections.singletonList(new RangeInitTask(metaProperty, type, map)));
            }
        }
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
                    + " must either be of parameterized type or have a JPA annotation declaring a targetEntity");
        return type;
    }

    protected Class getFieldTypeAccordingAnnotations(Field field) {
        throw new UnsupportedOperationException();
    }

    protected Range.Cardinality getCardinality(Field field) {
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

    protected String getInverseField(Field field) {
        return null;
    }

    protected MetadataObjectInfo<MetaProperty> loadCollectionProperty(MetaClassImpl metaClass, Field field) {
        Collection<MetadataObjectInitTask> tasks = new ArrayList<>();

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
        property.setAnnotatedElement(field);
        property.setDeclaringClass(field.getDeclaringClass());
        property.setJavaType(field.getType());

        return new MetadataObjectInfo<>(property, tasks);
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

    protected MetaClassImpl createClass(Class<?> clazz, String packageName, String className) {
        MetaClassImpl metaClass = (MetaClassImpl) session.getClass(clazz);
        if (metaClass != null) {
            return metaClass;
        } else if (packageName == null || clazz.getName().startsWith(packageName)) {
            metaClass = createMetaClass(packageName, className);
            metaClass.setJavaClass(clazz);

            Class<?> ancestor = clazz.getSuperclass();
            if (ancestor != null) {
                MetaClass ancestorClass = createClass(ancestor, packageName);
                if (ancestorClass != null) {
                    metaClass.addAncestor(ancestorClass);
                }
            }
            onClassLoaded(metaClass, clazz);

            return metaClass;
        } else {
            return null;
        }
    }

    protected class RangeInitTask implements MetadataObjectInitTask {

        private MetaProperty metaProperty;
        private Class rangeClass;
        private Map<String, Object> map;

        public RangeInitTask(MetaProperty metaProperty, Class rangeClass, Map<String, Object> map) {
            this.metaProperty = metaProperty;
            this.rangeClass = rangeClass;
            this.map = map;
        }

        @Override
        public String getWarning() {
            return String.format(
                    "Range for propery '%s' wasn't initialized (range class '%s')",
                    metaProperty.getName(), rangeClass.getName());
        }

        @Override
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
}