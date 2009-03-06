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
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.CollectionDatasourceWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.EnumerationContainer;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.datatypes.Enumiration;
import com.itmill.toolkit.ui.Select;
import com.itmill.toolkit.ui.AbstractSelect;
import com.itmill.toolkit.ui.OptionGroup;
import com.itmill.toolkit.data.Property;

import java.util.List;
import java.util.Arrays;

public class LookupField
    extends
        AbstractOptionsField<Select>
    implements
        com.haulmont.cuba.gui.components.LookupField, Component.Wrapper
{
    public LookupField() {
        this.component = new Select();
        component.setImmediate(true);
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
    }

    protected Object getKeyFromValue(Object value) {
        if (value instanceof Enum) {
            return value;
        } else {
            return (value instanceof Entity) ? ((Entity) value).getId() : value;
        }
    }

    protected <T> T getValueFromKey(Object key) {
        if (key == null) return null;
        if (key instanceof Enum) { return (T)key; }

        Object v;
        if (optionsDatasource != null) {
            v = (T) optionsDatasource.getItem(key);
        } else {
            v = key;
        }

        return (T) v;
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        component.setNullSelectionAllowed(!required);
    }
}