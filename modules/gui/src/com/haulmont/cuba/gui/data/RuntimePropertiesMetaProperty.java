/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.impl.AbstractRange;
import com.haulmont.chile.core.model.impl.ClassRange;
import com.haulmont.chile.core.model.impl.DatatypeRange;
import com.haulmont.chile.core.model.impl.MetadataObjectImpl;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.SetValueEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Specific MetaProperty for runtime property.
 *
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropertiesMetaProperty extends MetadataObjectImpl<MetaProperty> implements MetaProperty {

    private MetaClass metaClass;
    private Range range;
    private Class javaClass;

    private AnnotatedElement annotatedElement = new FakeAnnotatedElement();

    public RuntimePropertiesMetaProperty(MetaClass metaClass, String name, Class javaClass) {
        this.javaClass = javaClass;
        this.metaClass = metaClass;
        this.name = name;
        Session metadataSession = AppBeans.get(Metadata.class).getSession();
        if (SetValueEntity.class.isAssignableFrom(javaClass)) {
            range = new ClassRange(metadataSession.getClass(SetValueEntity.class));
            ((AbstractRange) range).setCardinality(Range.Cardinality.ONE_TO_ONE);
        } else if (Entity.class.isAssignableFrom(javaClass)) {
            range = new ClassRange(metadataSession.getClass(javaClass));
        } else {
            this.range = new DatatypeRange(Datatypes.getNN(javaClass));
        }
    }

    @Override
    public MetaModel getModel() {
        return metaClass.getModel();
    }

    @Override
    public MetaClass getDomain() {
        return metaClass;
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public Type getType() {
        return Type.DATATYPE;
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public MetaProperty getInverse() {
        return null;
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    @Override
    public Class<?> getJavaType() {
        return javaClass;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return null;
    }

    protected static class FakeAnnotatedElement implements AnnotatedElement {

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            return false;
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }
    }
}
