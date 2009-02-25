/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 30.12.2008 16:27:03
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;

public class AbstractField<T extends com.itmill.toolkit.ui.Field> extends AbstractComponent<T>{
    protected Datasource datasource;
    protected String property;
    private boolean editable;

    public Datasource getDatasource() {
        return datasource;
    }

    public String getProperty() {
        return property;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;
        this.property = property;

        final MetaClass metaClass = datasource.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);

        final ItemWrapper wrapper = new ItemWrapper(datasource, metaClass.getProperties());
        component.setPropertyDataSource(wrapper.getItemProperty(metaProperty));

        setRequired(metaProperty.isMandatory());
    }

    public boolean isRequired() {
        return component.isRequired();
    }

    public void setRequired(boolean required) {
        component.setRequired(required);
    }

    public <T> T getValue() {
        return (T) component.getValue();
    }

    public void setValue(Object value) {
        component.setValue(value);
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        component.setReadOnly(!editable);
    }
}
