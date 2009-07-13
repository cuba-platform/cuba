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
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import org.apache.commons.lang.BooleanUtils;

import java.util.Collection;

public class CheckBox
    extends
        AbstractField<com.itmill.toolkit.ui.CheckBox>
    implements
        com.haulmont.cuba.gui.components.CheckBox, Component.Wrapper {

    public CheckBox() {
        this.component = new com.itmill.toolkit.ui.CheckBox();
        attachListener(component);
        component.setImmediate(true);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    @Override
                    public Object getValue() {
                        return BooleanUtils.toBoolean((Boolean) super.getValue());
                    }
                };
            }
        };
    }
}