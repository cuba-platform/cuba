/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Metadata.NAME)
public class MetadataImpl implements Metadata {

    protected Log log = LogFactory.getLog(getClass());

    protected volatile Session session;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected MetadataTools tools;

    @Inject
    protected PersistentEntitiesMetadataLoader metadataLoader;

    @Inject
    protected Resources resources;

    @Inject
    protected MetadataBuildSupport metadataBuildSupport;

    private static final Pattern JAVA_CLASS_PATTERN = Pattern.compile("([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*");

    @Override
    public Session getSession() {
        if (session == null) {
            synchronized (this) {
                if (session == null) {
                    initMetadata();
                }
            }
        }
        return session;
    }

    @Override
    public ViewRepository getViewRepository() {
        return viewRepository;
    }

    @Override
    public ExtendedEntities getExtendedEntities() {
        return extendedEntities;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MetadataTools getTools() {
        return tools;
    }

    protected void loadMetadata(MetadataLoader loader, Map<String, List<String>> packages) {
        for (Map.Entry<String, List<String>> entry : packages.entrySet()) {
            loader.loadModel(entry.getKey(), entry.getValue());
        }
    }

    protected <T> T __create(Class<T> entityClass) {
        @SuppressWarnings("unchecked")
        Class<T> extClass = extendedEntities.getEffectiveClass(entityClass);
        try {
            T obj = extClass.newInstance();
            invokePostConstructMethods((Entity) obj);
            return obj;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void invokePostConstructMethods(Entity entity) throws InvocationTargetException, IllegalAccessException {
        List<Method> postConstructMethods = new ArrayList<>(4);
        List<String> methodNames = new ArrayList<>(4);
        Class clazz = entity.getClass();
        while (clazz != Object.class) {
            Method[] classMethods = clazz.getDeclaredMethods();
            for (Method method : classMethods) {
                if (method.isAnnotationPresent(PostConstruct.class) && !methodNames.contains(method.getName())) {
                    postConstructMethods.add(method);
                    methodNames.add(method.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }

        ListIterator<Method> iterator = postConstructMethods.listIterator(postConstructMethods.size());
        while (iterator.hasPrevious()) {
            Method method = iterator.previous();
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(entity);
        }
    }

    @Override
    public <T> T create(Class<T> entityClass) {
        return __create(entityClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(MetaClass metaClass) {
        return (T) __create(metaClass.getJavaClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(String entityName) {
        MetaClass metaClass = getSession().getClassNN(entityName);
        return (T) __create(metaClass.getJavaClass());
    }

    protected void initMetadata() {
        log.info("Initializing metadata");
        long startTime = System.currentTimeMillis();

        loadMetadata(metadataLoader, metadataBuildSupport.getEntityPackages());
        metadataLoader.postProcess();

        Session session = metadataLoader.getSession();

        initExtensionMetaAnnotations(session);

        Map<String, Map<String, String>> xmlAnnotations = metadataBuildSupport.getEntityAnnotations();
        for (MetaClass metaClass : session.getClasses()) {
            initMetaAnnotations(session, metaClass);
            addMetaAnnotationsFromXml(xmlAnnotations, metaClass);
        }

        replaceExtendedMetaClasses(session);

        this.session = new CachingMetadataSession(session);

        SessionImpl.setSerializationSupportSession(this.session);

        log.info("Metadata initialized in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    protected void replaceExtendedMetaClasses(Session session) {
        StopWatch sw = new Log4JStopWatch("Metadata.replaceExtendedMetaClasses");

        for (MetaModel model : session.getModels()) {
            MetaModelImpl modelImpl = (MetaModelImpl) model;

            List<Pair<MetaClass, MetaClass>> replaceMap = new ArrayList<>();
            for (MetaClass metaClass : modelImpl.getClasses()) {
                MetaClass effectiveMetaClass = session.getClass(extendedEntities.getEffectiveClass(metaClass));

                if (effectiveMetaClass != metaClass) {
                    replaceMap.add(new Pair<>(metaClass, effectiveMetaClass));
                }

                for (MetaProperty metaProperty : metaClass.getOwnProperties()) {
                    MetaPropertyImpl propertyImpl = (MetaPropertyImpl) metaProperty;

                    // replace domain
                    Class effectiveDomainClass = extendedEntities.getEffectiveClass(metaProperty.getDomain());
                    MetaClass effectiveDomainMeta = session.getClass(effectiveDomainClass);
                    if (metaProperty.getDomain() != effectiveDomainMeta) {
                        propertyImpl.setDomain(effectiveDomainMeta);
                    }

                    if (metaProperty.getRange().isClass()) {
                        // replace range class
                        ClassRange range = (ClassRange) metaProperty.getRange();

                        Class effectiveRangeClass = extendedEntities.getEffectiveClass(range.asClass());
                        MetaClass effectiveRangeMeta = session.getClass(effectiveRangeClass);
                        if (effectiveRangeMeta != range.asClass()) {
                            ClassRange newRange = new ClassRange(effectiveRangeMeta);
                            newRange.setCardinality(range.getCardinality());
                            newRange.setOrdered(range.isOrdered());

                            ((MetaPropertyImpl) metaProperty).setRange(newRange);
                        }
                    }
                }
            }

            for (Pair<MetaClass, MetaClass> replace : replaceMap) {
                MetaClass replacedMetaClass = replace.getFirst();
                extendedEntities.registerReplacedMetaClass(replacedMetaClass);

                MetaClassImpl effectiveMetaClass = (MetaClassImpl) replace.getSecond();
                modelImpl.registerClass(replacedMetaClass.getName(), replacedMetaClass.getJavaClass(), effectiveMetaClass);
            }
        }

        sw.stop();
    }

    /**
     * Initialize connections between extended and base entities.
     *
     * @param session metadata session which is being initialized
     */
    protected void initExtensionMetaAnnotations(Session session) {
        for (MetaClass metaClass : session.getClasses()) {
            Class<?> javaClass = metaClass.getJavaClass();

            List<Class> superClasses = new ArrayList<>();
            Extends extendsAnnotation = javaClass.getAnnotation(Extends.class);
            while (extendsAnnotation != null) {
                Class<? extends Entity> superClass = extendsAnnotation.value();
                superClasses.add(superClass);
                extendsAnnotation = superClass.getAnnotation(Extends.class);
            }

            for (Class superClass : superClasses) {
                metaClass.getAnnotations().put(Extends.class.getName(), superClass);

                MetaClass superMetaClass = session.getClassNN(superClass);

                Class<?> extendedByClass = (Class) superMetaClass.getAnnotations().get(ExtendedBy.class.getName());
                if (extendedByClass != null && !javaClass.equals(extendedByClass)) {
                    if (javaClass.isAssignableFrom(extendedByClass))
                        continue;
                    else if (!extendedByClass.isAssignableFrom(javaClass))
                        throw new IllegalStateException(superClass + " is already extended by " + extendedByClass);
                }

                superMetaClass.getAnnotations().put(ExtendedBy.class.getName(), javaClass);
            }
        }
    }

    /**
     * Initialize entity annotations from class-level Java annotations.
     * <p>Can be overridden in application projects to handle application-specific annotations.</p>
     *
     * @param session   metadata session which is being initialized
     * @param metaClass MetaClass instance to assign annotations
     */
    protected void initMetaAnnotations(Session session, MetaClass metaClass) {
        addMetaAnnotation(metaClass, NamePattern.class.getName(),
                new AnnotationValue() {
                    @Override
                    public Object get(Class<?> javaClass) {
                        NamePattern annotation = javaClass.getAnnotation(NamePattern.class);
                        return annotation == null ? null : annotation.value();
                    }
                }
        );

        addMetaAnnotation(metaClass, EnableRestore.class.getName(),
                new AnnotationValue() {
                    @Override
                    public Object get(Class<?> javaClass) {
                        EnableRestore annotation = javaClass.getAnnotation(EnableRestore.class);
                        return annotation == null ? null : annotation.value();
                    }
                }
        );

        addMetaAnnotation(metaClass, TrackEditScreenHistory.class.getName(),
                new AnnotationValue() {
                    @Override
                    public Object get(Class<?> javaClass) {
                        TrackEditScreenHistory annotation = javaClass.getAnnotation(TrackEditScreenHistory.class);
                        return annotation == null ? null : annotation.value();
                    }
                }
        );

        // @SystemLevel is not propagated down to the hierarchy
        Class<?> javaClass = metaClass.getJavaClass();
        SystemLevel annotation = javaClass.getAnnotation(SystemLevel.class);
        if (annotation != null)
            metaClass.getAnnotations().put(SystemLevel.class.getName(), annotation.value());
    }

    /**
     * Add a meta-annotation from class annotation.
     *
     * @param metaClass       entity's meta-class
     * @param name            meta-annotation name
     * @param annotationValue annotation value extractor instance
     */
    protected void addMetaAnnotation(MetaClass metaClass, String name, AnnotationValue annotationValue) {
        Object value = annotationValue.get(metaClass.getJavaClass());
        if (value == null) {
            for (MetaClass ancestor : metaClass.getAncestors()) {
                value = annotationValue.get(ancestor.getJavaClass());
                if (value != null)
                    break;
            }
        }
        if (value != null) {
            metaClass.getAnnotations().put(name, value);
        }
    }

    /**
     * Initialize entity annotations from definition in <code>metadata.xml</code>.
     * <p>Can be overridden in application projects to handle application-specific annotations.</p>
     *
     * @param xmlAnnotations map of class name to annotations map
     * @param metaClass      MetaClass instance to assign annotations
     */
    protected void addMetaAnnotationsFromXml(Map<String, Map<String, String>> xmlAnnotations, MetaClass metaClass) {
        Map<String, String> classAnnotations = xmlAnnotations.get(metaClass.getJavaClass().getName());
        if (classAnnotations != null) {
            for (Map.Entry<String, String> annEntry : classAnnotations.entrySet()) {
                metaClass.getAnnotations().put(annEntry.getKey(), inferMetaAnnotationType(annEntry.getValue()));
            }
        }
    }

    protected Object inferMetaAnnotationType(String str) {
        Object val;
        if (str != null && (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")))
            val = Boolean.valueOf(str);
        else if (str != null && JAVA_CLASS_PATTERN.matcher(str).matches()) {
            try {
                val = ReflectionHelper.loadClass(str);
            } catch (ClassNotFoundException e) {
                val = str;
            }
        } else
            val = str;
        return val;
    }

    @Override
    public MetaModel getModel(String name) {
        return getSession().getModel(name);
    }

    @Override
    public Collection<MetaModel> getModels() {
        return getSession().getModels();
    }

    @Nullable
    @Override
    public MetaClass getClass(String name) {
        return getSession().getClass(name);
    }

    @Override
    public MetaClass getClassNN(String name) {
        return getSession().getClassNN(name);
    }

    @Nullable
    @Override
    public MetaClass getClass(Class<?> clazz) {
        return getSession().getClass(clazz);
    }

    @Override
    public MetaClass getClassNN(Class<?> clazz) {
        return getSession().getClassNN(clazz);
    }

    @Override
    public Collection<MetaClass> getClasses() {
        return getSession().getClasses();
    }

    /**
     * Annotation value extractor.
     * <p/> Implementations are supposed to be passed to {@link #addMetaAnnotation(MetaClass, String, AnnotationValue)}
     * method.
     */
    protected interface AnnotationValue {
        Object get(Class<?> javaClass);
    }
}