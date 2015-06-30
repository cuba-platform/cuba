/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.impl.ClassRange;
import com.haulmont.chile.core.model.impl.DatatypeRange;
import com.haulmont.chile.core.model.impl.MetadataObjectImpl;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Specific MetaProperty for dynamic attribute.
 *
 * @author devyatkin
 * @version $Id$
 */
public class DynamicAttributesMetaProperty extends MetadataObjectImpl implements MetaProperty {
    protected final MetaClass metaClass;
    protected final transient Range range;
    protected final Class javaClass;
    protected final Boolean mandatory;
    protected final AnnotatedElement annotatedElement = new FakeAnnotatedElement();
    protected final CategoryAttribute attribute;

    public DynamicAttributesMetaProperty(MetaClass metaClass, CategoryAttribute attribute) {
        this.attribute = attribute;
        this.javaClass = DynamicAttributesUtils.getAttributeClass(attribute);
        this.metaClass = metaClass;
        this.name = DynamicAttributesUtils.encodeAttributeCode(attribute.getCode());
        this.mandatory = attribute.getRequired();

        Metadata metadata = AppBeans.get(Metadata.NAME);
        Session metadataSession = metadata.getSession();
        if (Entity.class.isAssignableFrom(javaClass)) {
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
        return Boolean.TRUE.equals(mandatory);
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

    protected static class FakeAnnotatedElement implements AnnotatedElement, Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DynamicAttributesMetaProperty)) return false;

        DynamicAttributesMetaProperty that = (DynamicAttributesMetaProperty) o;

        return metaClass.equals(that.metaClass) && name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return 31 * metaClass.hashCode() + name.hashCode();
    }

    public CategoryAttribute getAttribute() {
        return attribute;
    }
}
