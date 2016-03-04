/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.web.toolkit.ui.CubaOptionGroup;
import com.haulmont.cuba.web.toolkit.ui.client.optiongroup.OptionGroupOrientation;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author abramov
 */
public class WebOptionsGroup extends WebAbstractOptionsBase<CubaOptionGroup> implements OptionsGroup {

    protected Orientation orientation = Orientation.VERTICAL;

    public WebOptionsGroup() {
        component = new CubaOptionGroup() {
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

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        checkNotNull(orientation, "Orientation must not be null");

        if (orientation != this.orientation) {
            if (orientation == Orientation.HORIZONTAL) {
                component.setOrientation(OptionGroupOrientation.HORIZONTAL);
            } else {
                component.setOrientation(OptionGroupOrientation.VERTICAL);
            }
            this.orientation = orientation;
        }
    }
}