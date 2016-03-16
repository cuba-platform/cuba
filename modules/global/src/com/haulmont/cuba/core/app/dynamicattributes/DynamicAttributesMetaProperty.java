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
 */
public class DynamicAttributesMetaProperty extends MetadataObjectImpl implements MetaProperty {

    private static final long serialVersionUID = 839160118855669248L;

    protected final MetaClass metaClass;
    protected final transient Range range;
    protected final Class javaClass;
    protected final Boolean mandatory;
    protected final AnnotatedElement annotatedElement = new FakeAnnotatedElement();
    protected final CategoryAttribute attribute;
    protected final Type type;

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
            this.type = Type.ASSOCIATION;
        } else {
            this.range = new DatatypeRange(Datatypes.getNN(javaClass));
            this.type = Type.DATATYPE;
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
        return type;
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
