/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 15.04.2009 16:19:17
 * $Id: MetaPropertyPath.java 2555 2010-08-30 12:58:15Z krivopustov $
 */

package com.haulmont.chile.core.model;

import com.haulmont.chile.core.model.utils.MetadataUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang.text.StrBuilder;

/**
 * Object representing a relative path to a property from certain MetaClass
 */
public class MetaPropertyPath implements Serializable {

    private static final long serialVersionUID = -3149651267513333787L;

    private MetaClass metaClass;
    private MetaProperty[] metaProperties;

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
        return MetadataUtils.getTypeClass(metaProperties[metaProperties.length - 1]);
    }

    protected String[] path;

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
}
