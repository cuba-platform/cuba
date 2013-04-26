/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;

import java.util.Collection;

/**
 * @author abramov
 * @version $Id$
 */
public class WebTextField
        extends
            WebAbstractTextField<com.vaadin.ui.TextField>
        implements
            TextField, Component.Wrapper {

    protected Datatype datatype;
    protected Formatter formatter;

    protected boolean trimming = true;

    @Override
    protected com.vaadin.ui.TextField createTextFieldImpl() {
        return new com.vaadin.ui.TextField();
    }

    @Override
    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
    protected Datatype getActualDatatype() {
        if (metaProperty != null) {
            return metaProperty.getRange().isDatatype() ? metaProperty.getRange().asDatatype() : null;
        } else if (datatype != null) {
            return datatype;
        } else {
            return Datatypes.getNN(String.class);
        }
    }

    @Override
    public boolean isTrimming() {
        return trimming;
    }

    @Override
    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new TextFieldItemWrapper(datasource, propertyPaths);
    }

    protected class TextFieldItemWrapper extends ItemWrapper {
        public TextFieldItemWrapper(Object item, Collection<MetaPropertyPath> properties) {
            super(item, properties);
        }

        @Override
        protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            return new TextFieldPropertyWrapper(item, propertyPath);
        }
    }

    protected class TextFieldPropertyWrapper extends TextPropertyWrapper {

        public TextFieldPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            super(item, propertyPath);
        }

        @Override
        public String getFormattedValue() {
            if (formatter != null) {
                Object value = getValue();
                if (value instanceof Instance)
                    value = ((Instance) value).getInstanceName();
                return formatter.format(value);
            } else
                return super.getFormattedValue();
        }
    }
}