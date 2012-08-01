/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang.BooleanUtils;

import java.util.Collection;

public class WebCheckBox
    extends
        WebAbstractField<com.haulmont.cuba.web.toolkit.ui.CheckBox>
    implements
        CheckBox, Component.Wrapper {

    private VerticalLayout composition;

    public WebCheckBox() {
        this.component = new com.haulmont.cuba.web.toolkit.ui.CheckBox();
        this.composition = new VerticalLayout();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);

        composition.addComponent(component);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths, DsManager dsManager) {
        return new ItemWrapper(datasource, propertyPaths, dsManager) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
                return new PropertyWrapper(item, propertyPath, dsManager) {
                    @Override
                    public Object getValue() {
                        return BooleanUtils.toBoolean((Boolean) super.getValue());
                    }
                };
            }
        };
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return composition;
    }

    @Override
    public void setValue(Object value) {
        if (value == null)
            super.setValue(Boolean.FALSE);
        else
            super.setValue(value);
    }
}