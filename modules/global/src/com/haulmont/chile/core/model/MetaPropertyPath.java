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

package com.haulmont.chile.core.model;

import org.apache.commons.lang.text.StrBuilder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Object representing a relative path to a property from certain MetaClass
 *
 */
public class MetaPropertyPath implements Serializable {

    private static final long serialVersionUID = -3149651267513333787L;

    private MetaClass metaClass;
    private MetaProperty[] metaProperties;
    private String[] path;

    public MetaPropertyPath(MetaClass metaClass, MetaProperty... metaProperties) {
        this.metaClass = metaClass;
        this.metaProperties = metaProperties;

        this.path = new String[metaProperties.length];
        for (int i = 0; i < metaProperties.length; i++) {
            path[i] = metaProperties[i].getName();
        }
    }

    public MetaPropertyPath(MetaPropertyPath parentPath, MetaProperty... addProperties) {
        this.metaClass = parentPath.getMetaClass();

        this.metaProperties = new MetaProperty[parentPath.metaProperties.length + addProperties.length];
        System.arraycopy(parentPath.metaProperties, 0, this.metaProperties, 0, parentPath.metaProperties.length);
        System.arraycopy(addProperties, 0, this.metaProperties, parentPath.metaProperties.length, addProperties.length);

        this.path = new String[this.metaProperties.length];
        for (int i = 0; i < this.metaProperties.length; i++) {
            this.path[i] = this.metaProperties[i].getName();
        }
    }

    /**
     * MetaClass which is the path origin
     */
    public MetaClass getMetaClass() {
        return metaClass;
    }

    /**
     * Array of MetaProperties between the originating MetaClass and target MetaProperty 
     */
    public MetaProperty[] getMetaProperties() {
        return metaProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetaPropertyPath that = (MetaPropertyPath) o;

        if (!metaClass.equals(that.metaClass)) return false;
        if (!Arrays.equals(metaProperties, that.metaProperties)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = metaClass.hashCode();
        result = 31 * result + Arrays.hashCode(metaProperties);
        return result;
    }

    /**
     * Range of the target MetaProperty
     */
    public Range getRange() {
        return metaProperties[metaProperties.length - 1].getRange();
    }

    /**
     * Java class of the target MetaProperty range
     */
    public Class getRangeJavaClass() {
        if (metaProperties.length < 1) {
            throw new RuntimeException("Empty property path at metaclass " + metaClass.getName() + " - possibly wrong property name");
        }
        return getTypeClass(metaProperties[metaProperties.length - 1]);
    }

    /**
     * Path as String array
     */
    public String[] getPath() {
        return path;
    }

    /**
     * Target MetaProperty
     */
    public MetaProperty getMetaProperty() {
        return metaProperties[metaProperties.length - 1];
    }

    /**
     * Tests if this path is a nested property of the given path.
     */
    public boolean startsWith(MetaPropertyPath other) {
        if (other.getPath().length > path.length)
            return false;
        MetaProperty[] subarray = Arrays.copyOf(this.metaProperties, other.getMetaProperties().length);
        return Arrays.equals(subarray, other.metaProperties);
    }

    @Override
    public String toString() {
        return new StrBuilder().appendWithSeparators(path, ".").toString();
    }

    private Class getTypeClass(MetaProperty metaProperty) {
        Range range = metaProperty.getRange();
        if (range.isDatatype()) {
            return range.asDatatype().getJavaClass();
        } else if (range.isClass()) {
            return range.asClass().getJavaClass();
        } else if (range.isEnum()) {
            return range.asEnumeration().getJavaClass();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}