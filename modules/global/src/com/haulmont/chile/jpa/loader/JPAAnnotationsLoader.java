/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.chile.jpa.loader;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.loader.ChileAnnotationsLoader;
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
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author krivopustov
 * @version $Id$
 */
public class JPAAnnotationsLoader extends ChileAnnotationsLoader {

    private Log log = LogFactory.getLog(JPAMetadataLoader.class);

    public JPAAnnotationsLoader(Session session) {
        super(session);
    }

    @Override
    protected List<Class<?>> getClasses(Resource[] resources) {
        List<Class<?>> result = super.getClasses(resources);

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
                        result.add(c);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        return result;
    }

    @Override
    protected boolean isMetaPropertyField(Field field) {
        return field.isAnnotationPresent(Column.class)
                || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(ManyToMany.class)
                || field.isAnnotationPresent(Embedded.class)
                || super.isMetaPropertyField(field);
    }

    @Override
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

    @Override
    protected Class getTypeOverride(AnnotatedElement element) {
        Temporal temporal = element.getAnnotation(Temporal.class);
        if (temporal != null && temporal.value().equals(TemporalType.DATE))
            return java.sql.Date.class;
        else if (temporal != null && temporal.value().equals(TemporalType.TIME))
            return java.sql.Time.class;
        else
            return null;
    }

    @Override
    protected boolean isMandatory(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        OneToOne oneToOneAnnotation = field.getAnnotation(OneToOne.class);
        OneToMany oneToManyAnnotation = field.getAnnotation(OneToMany.class);
        ManyToOne manyToOneAnnotation = field.getAnnotation(ManyToOne.class);
        ManyToMany manyToManyAnnotation = field.getAnnotation(ManyToMany.class);

        if (columnAnnotation != null) {
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
            return super.isMandatory(field);
        }
    }

    @Override
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
            return super.getCardinality(field);
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

    @Override
    protected MetaClassImpl createClass(Class<?> clazz, String packageName) {
        if (Object.class.equals(clazz)) return null;

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        MappedSuperclass mappedSuperclassAnnotation = clazz.getAnnotation(MappedSuperclass.class);

        MetaClass metaClassAnntotation = clazz.getAnnotation(MetaClass.class);
        Embeddable embeddableAnnotation = clazz.getAnnotation(Embeddable.class);

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

        return createClass(clazz, packageName, className);
    }

    @Override
    protected void onPropertyLoaded(MetaProperty metaProperty, Field field) {
        super.onPropertyLoaded(metaProperty, field);

        if (isPersistent(field))
            metaProperty.getAnnotations().put("persistent", true);

        if (isPrimaryKey(field)) {
            metaProperty.getAnnotations().put("primaryKey", true);
            metaProperty.getDomain().getAnnotations().put("primaryKey", metaProperty.getName());
        }

        if (isEmbedded(field))
            metaProperty.getAnnotations().put("embedded", true);

        Column column = field.getAnnotation(Column.class);
        Lob lob = field.getAnnotation(Lob.class);
        if (column != null && column.length() != 0 && lob == null) {
            metaProperty.getAnnotations().put("length", column.length());
        }

        Temporal temporal = field.getAnnotation(Temporal.class);
        if (temporal != null) {
            metaProperty.getAnnotations().put("temporal", temporal.value());
        }
    }

    protected boolean isPrimaryKey(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    protected boolean isEmbedded(Field field) {
        return field.isAnnotationPresent(Embedded.class);
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