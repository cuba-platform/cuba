/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractMetadata implements Metadata {

    protected Log log = LogFactory.getLog(getClass());

    protected volatile Session session;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected MetadataTools tools;

    @Inject
    private PersistentEntitiesMetadataLoader persistentEntitiesMetadataLoader;

    @Inject
    private TransientEntitiesMetadataLoader transientEntitiesMetadataLoader;

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

    @Override
    public MetadataTools getTools() {
        return tools;
    }

    protected void loadMetadata(MetadataLoader loader, Collection<String> packages) {
        for (String p : packages) {
            loader.loadPackage(p, p);
        }
    }

    protected <T> T __create(Class<T> entityClass) {
        Class<T> extClass = extendedEntities.getEffectiveClass(entityClass);
        try {
            T obj = extClass.newInstance();
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T create(Class<T> entityClass) {
        return (T) __create(entityClass);
    }

    public <T> T create(MetaClass metaClass) {
        return (T) __create(metaClass.getJavaClass());
    }

    public <T> T create(String entityName) {
        MetaClass metaClass = getSession().getClassNN(entityName);
        return (T) __create(metaClass.getJavaClass());
    }

    protected void initMetadata() {
        log.info("Initializing metadata");
        long startTime = System.currentTimeMillis();

        MetadataBuildInfo metadataBuildInfo = getMetadataBuildInfo();

        loadMetadata(persistentEntitiesMetadataLoader, metadataBuildInfo.getPersistentEntitiesPackages());
        persistentEntitiesMetadataLoader.postProcess();

        Session session = persistentEntitiesMetadataLoader.getSession();

        transientEntitiesMetadataLoader.setSession(session);
        loadMetadata(transientEntitiesMetadataLoader, metadataBuildInfo.getTransientEntitiesPackages());
        transientEntitiesMetadataLoader.postProcess();

        initExtensionMetaAnnotations(session);

        for (MetaClass metaClass : session.getClasses()) {
            initMetaAnnotations(session, metaClass);
            addMetaAnnotationsFromXml(metadataBuildInfo.getEntityAnnotations(), metaClass);
        }

        this.session = new CachingMetadataSession(session);
        log.info("Metadata initialized in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    protected abstract MetadataBuildInfo getMetadataBuildInfo();

    /**
     * Initialize connections between extended and base entities.
     *
     * @param session metadata session which is being initialized
     */
    protected void initExtensionMetaAnnotations(Session session) {
        for (MetaClass metaClass : session.getClasses()) {
            Class<?> javaClass = metaClass.getJavaClass();

            Extends extendsAnnotation = javaClass.getAnnotation(Extends.class);
            if (extendsAnnotation != null) {
                Class<? extends Entity> superClass = extendsAnnotation.value();
                metaClass.getAnnotations().put(Extends.class.getName(), superClass);

                MetaClass superMetaClass = session.getClass(superClass);
                if (superMetaClass == null)
                    throw new IllegalStateException("No meta class found for " + superClass);

                Object extendedBy = superMetaClass.getAnnotations().get(ExtendedBy.class.getName());
                if (extendedBy != null && !javaClass.equals(extendedBy))
                    throw new IllegalStateException(superClass + " is already extended by " + extendedBy);

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

        addMetaAnnotation(metaClass, SystemLevel.class.getName(),
                new AnnotationValue() {
                    @Override
                    public Object get(Class<?> javaClass) {
                        SystemLevel annotation = javaClass.getAnnotation(SystemLevel.class);
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
    }

    /**
     * Add a meta-annotation from class annotation.
     *
     * @param metaClass         entity's meta-class
     * @param name              meta-annotation name
     * @param annotationValue   annotation value extractor instance
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
     * @param xmlAnnotations    map of class name to annotations map
     * @param metaClass MetaClass instance to assign annotations
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
        else if (JAVA_CLASS_PATTERN.matcher(str).matches()) {
            try {
                val = ReflectionHelper.loadClass(str);
            } catch (ClassNotFoundException e) {
                val = str;
            }
        } else
            val = str;
        return val;
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
