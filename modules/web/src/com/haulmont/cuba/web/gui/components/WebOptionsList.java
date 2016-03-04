/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.OptionsList;
import com.haulmont.cuba.web.toolkit.ui.CubaListSelect;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;

/**
 * @author petunin
 */
public class WebOptionsList extends WebAbstractOptionsBase<CubaListSelect> implements OptionsList {
    public WebOptionsList() {
        component = new CubaListSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {

                    @Override
                    public Class getType() {
                        // we ourselves convert values in this property adapter
                        return Object.class;
                    }

                    @Override
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    @Override
                    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });
            }
        };
        attachListener(component);
        initDefaults(component);
    }
}