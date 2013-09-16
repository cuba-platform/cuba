/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model;

import org.apache.commons.lang.text.StrBuilder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Object representing a relative path to a property from certain MetaClass
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
        int i = 0;
        for (MetaProperty metaProperty : metaProperties) {
            path[i] = metaProperty.getName();
            i++;
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
     * Array of MetaProperties between the originating MetaClass and target MetaProperty
     */
    public MetaProperty[] get() {
        return metaProperties;
    }

    /**
     * Target MetaProperty
     */
    public MetaProperty getMetaProperty() {
        return metaProperties[metaProperties.length - 1];
    }

    public String toString() {
        StrBuilder sb = new StrBuilder();
        return sb.appendWithSeparators(path, ".").toString();
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
