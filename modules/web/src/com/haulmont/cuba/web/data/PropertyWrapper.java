/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:16:49
 * $Id$
 */
package com.haulmont.cuba.web.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.gui.MetadataHelper;
import com.itmill.toolkit.data.Property;

public class PropertyWrapper implements Property{
    private boolean readOnly;
    private Object item;
    private MetaProperty metaProperty;

    public PropertyWrapper(Object item, MetaProperty metaProperty) {
        this.item = item;
        this.metaProperty = metaProperty;
    }

    public Object getValue() {
        return ((Instance) item).getValue(metaProperty.getName());
    }

    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        ((Instance) item).setValue(metaProperty.getName(), newValue);
    }

    public Class getType() {
        return MetadataHelper.getPropertyTypeClass(metaProperty);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean newStatus) {
        readOnly = newStatus;
    }

    @Override
    public String toString() {
        final Object value = getValue();
        return metaProperty.getRange().isDatatype() ?
                metaProperty.getRange().asDatatype().format(value) :
                value == null ? null : value.toString();
    }
}
