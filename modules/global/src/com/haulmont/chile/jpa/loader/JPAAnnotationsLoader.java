/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.chile.jpa.loader;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.loader.ChileAnnotationsLoader;
import com.haulmont.chile.core.loader.ClassMetadataLoader;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.MetaClassImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;

import javax.persistence.*;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author krivopustov
 * @version $Id$
 */
public class JPAAnnotationsLoader extends ChileAnnotationsLoader implements ClassMetadataLoader {

    private Log log = LogFactory.getLog(JPAMetadataLoader.class);

    public JPAAnnotationsLoader(Session session) {
        super(session);
    }

    protected List<Class<?>> getClasses(Resource[] resources) {
        List<Class<?>> annotated = new ArrayList<Class<?>>();

        for (Resource resource : resources) {
            if (resource.isReadable()) {
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

                    boolean isEntity = annotationMetadata.isAnnotated(MappedSuperclass.class.getName()) ||
                            annotationMetadata.isAnnotated(Entity.class.getName());

                    boolean isEmbeddable = annotationMetadata.isAnnotated(Embeddable.class.getName()) &&
                            annotationMetadata.isAnnotated(MetaClass.class.getName());

                    boolean isAnnotated = isEntity || isEmbeddable;

                    if (isAnnotated) {
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

    protected boolean isMetaPropertyField(Field field) {
        final com.haulmont.chile.core.annotations.MetaProperty metaPropertyAnnotation =
                field.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);

        final Column columnAnnotation = field.getAnnotation(Column.class);

        final OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        final OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        final ManyToOne manyToOneAnnotation = field.getAnnotation(ManyToOne.class);
        final ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);
        final Embedded embeddedAnnotation = field.getAnnotation(Embedded.class);

        final boolean annotated =
                metaPropertyAnnotation != null ||
                        columnAnnotation != null ||
                        oneToOneAnnotation != null ||
                        oneToManyAnnotation != null ||
                        manyToOneAnnotation != null ||
                        manyToManyAnnotation != null ||
                        embeddedAnnotation != null;

        final String name = field.getName();
        return annotated && !"serialVersionUID".equals(name);
    }

    protected Class getFieldTypeAccordingAnnotations(Field field) {
        final OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        final OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        final ManyToOne manyToOneAnnotation = field.getAnnotation(ManyToOne.class);
        final ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);

        if (oneToOneAnnotation != null) {
            return oneToOneAnnotation.targetEntity();
        } else if (oneToManyAnnotation != null) {
            return oneToManyAnnotation.targetEntity();
        } else if (manyToOneAnnotation != null) {
            return manyToOneAnnotation.targetEntity();
        } else if (manyToManyAnnotation != null) {
            return manyToManyAnnotation.targetEntity();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    protected Class getTypeOverride(AnnotatedElement element) {
        Temporal temporal = element.getAnnotation(Temporal.class);
        //todo refactor conditions
        if (temporal != null && temporal.value().equals(TemporalType.DATE))
            return java.sql.Date.class;
        else if (temporal != null && temporal.value().equals(TemporalType.TIME))
            return java.sql.Time.class;
        else
            return null;
    }

    protected boolean isMandatory(Field field) {
        final com.haulmont.chile.core.annotations.MetaProperty metaPropertyAnnotation =
                field.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class);

        final Column columnAnnotation = field.getAnnotation(Column.class);
        final OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        final OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        final ManyToOne manyToOneAnnotation = field.getAnnotation(ManyToOne.class);
        final ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);

        if (metaPropertyAnnotation != null) {
            return metaPropertyAnnotation.mandatory();
        } else if (columnAnnotation != null) {
            return !columnAnnotation.nullable();
        } else if (oneToOneAnnotation != null) {
            return !oneToOneAnnotation.optional();
        } else if (oneToManyAnnotation != null) {
            return false;
        } else if (manyToOneAnnotation != null) {
            return !manyToOneAnnotation.optional();
        } else if (manyToManyAnnotation != null) {
            return false;
        } else {
            return false;
        }
    }

    protected Range.Cardinality getCardinality(Field field) {
        final OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        final OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        final ManyToOne manyToOneAnnotation = field.getAnnotation(ManyToOne.class);
        final ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);
        final Embedded embeddedAnnotation = field.getAnnotation(Embedded.class);

        if (oneToOneAnnotation != null) {
            return Range.Cardinality.ONE_TO_ONE;
        } else if (oneToManyAnnotation != null) {
            return Range.Cardinality.ONE_TO_MANY;
        } else if (manyToOneAnnotation != null) {
            return Range.Cardinality.MANY_TO_ONE;
        } else if (manyToManyAnnotation != null) {
            return Range.Cardinality.MANY_TO_MANY;
        } else if (embeddedAnnotation != null) {
            return Range.Cardinality.ONE_TO_ONE;
        } else {
            return null;
        }
    }

    @Override
    protected String getInverseField(Field field) {
        OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        if (oneToManyAnnotation != null)
            return isBlank(oneToManyAnnotation.mappedBy()) ? null : oneToManyAnnotation.mappedBy();

        ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);
        if (manyToManyAnnotation != null)
            return isBlank(manyToManyAnnotation.mappedBy()) ? null : manyToManyAnnotation.mappedBy();

        return null;
    }

    protected MetaClassImpl __createClass(Class<?> clazz, String modelName) {
        if (Object.class.equals(clazz)) return null;

        final Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        final MappedSuperclass mappedSuperclassAnnotation = clazz.getAnnotation(MappedSuperclass.class);

        final MetaClass metaClassAnntotation = clazz.getAnnotation(MetaClass.class);
        final Embeddable embeddableAnnotation = clazz.getAnnotation(Embeddable.class);

        if ((entityAnnotation == null && mappedSuperclassAnnotation == null) &&
                (embeddableAnnotation == null) && (metaClassAnntotation == null)) {
            log.trace(String.format("Class '%s' isn't annotated as metadata entity, ignore it", clazz.getName()));
            return null;
        }

        String className = null;
        if (entityAnnotation != null) {
            className = entityAnnotation.name();
        } else if (metaClassAnntotation != null) {
            className = metaClassAnntotation.name();
        }

        if (StringUtils.isEmpty(className)) {
            className = clazz.getSimpleName();
        }

        return __createClass(clazz, modelName, className);
    }

    @Override
    protected void onPropertyLoaded(MetaProperty metaProperty, Field field) {
        super.onPropertyLoaded(metaProperty, field);

        if (isPersistent(field))
            metaProperty.getAnnotations().put("persistent", true);

        Column column = field.getAnnotation(Column.class);
        if (column != null && column.length() != 0) {
            metaProperty.getAnnotations().put("length", column.length());
        }

        Temporal temporal = field.getAnnotation(Temporal.class);
        if (temporal != null) {
            metaProperty.getAnnotations().put("temporal", temporal.value());
        }
    }

    protected boolean isPersistent(Field field) {
        return  field.isAnnotationPresent(Column.class)
                || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(ManyToMany.class)
                || field.isAnnotationPresent(Embedded.class);
    }
}