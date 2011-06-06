/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.impl.AbstractRange;
import com.haulmont.chile.core.model.impl.ClassRange;
import com.haulmont.chile.core.model.impl.DatatypeRange;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.sys.SetValueEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */

public class RuntimePropertiesMetaProperty implements MetaProperty {
    private MetaClass metaClass;
    private String name;
    private Range range;
    private Class javaClass;

    private AnnotatedElement annotatedElement = new FakeAnnotadetElement();

    public RuntimePropertiesMetaProperty(MetaClass metaClass, String name, Class javaClass) {
        this.javaClass = javaClass;
        this.metaClass = metaClass;
        this.name = name;
        if (SetValueEntity.class.isAssignableFrom(javaClass)) {
            range = new ClassRange(MetadataProvider.getSession().getClass(SetValueEntity.class));
            ((AbstractRange) range).setCardinality(Range.Cardinality.ONE_TO_ONE);
        } else if (Entity.class.isAssignableFrom(javaClass)) {
            range = new ClassRange(MetadataProvider.getSession().getClass(javaClass));
        } else {
            this.range = new DatatypeRange(Datatypes.get(javaClass));
        }
    }

    public MetaModel getModel() {
        return metaClass.getModel();
    }

    public MetaClass getDomain() {
        return metaClass;
    }

    public Range getRange() {
        return range;
    }

    public Type getType() {
        return Type.DATATYPE;
    }

    public boolean isMandatory() {
        return false;
    }

    public boolean isReadOnly() {
        return false;
    }

    public MetaProperty getInverse() {
        return null;
    }

    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    public Class<?> getJavaType() {
        return javaClass;
    }

    public Class<?> getDeclaringClass() {
        return null;
    }

    public MetaProperty getAncestor() {
        return null;
    }

    public Collection<MetaProperty> getAncestors() {
        return null;
    }

    public Collection<MetaProperty> getDescendants() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return null;
    }

    public String getCaption() {
        return name;
    }

    public String getDescription() {
        return null;
    }

    public UUID getUUID() {
        return null;
    }

    public Map<String, Object> getAnnotations() {
        return null;
    }

    protected class FakeAnnotadetElement implements AnnotatedElement {

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
