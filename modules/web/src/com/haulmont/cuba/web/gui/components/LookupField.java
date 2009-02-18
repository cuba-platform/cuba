/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.data.CollectionDatasourceWrapper;
import com.itmill.toolkit.ui.Select;

public class LookupField
    extends
        AbstractField<Select>
    implements
        com.haulmont.cuba.gui.components.LookupField, Component.Wrapper {

    public LookupField() {
        this.component = new Select();
        component.setImmediate(true);
    }

    public void setLookupDatasource(CollectionDatasource datasource) {
        component.setContainerDataSource(new CollectionDatasourceWrapper(datasource, true));
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        component.setNullSelectionAllowed(!required);
    }
}