/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;

import java.util.Collection;

/**
 * @author abramov
 * @version $Id$
 */
public class WebCheckBox
        extends
        WebAbstractField<com.haulmont.cuba.web.toolkit.ui.CheckBox>
        implements
        CheckBox {

    public WebCheckBox() {
        this.component = new com.haulmont.cuba.web.toolkit.ui.CheckBox();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    @Override
                    public Object getValue() {
                        Object value = super.getValue();
                        if (value == null && propertyPath.getRange().isDatatype()
                                && propertyPath.getRange().asDatatype().equals(Datatypes.get(Boolean.class))) {
                            value = Boolean.FALSE;
                        }
                        return value;
                    }
                };
            }
        };
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            super.setValue(Boolean.FALSE);
        } else {
            super.setValue(value);
        }
    }
}