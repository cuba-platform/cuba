/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.WebDateField;
import com.vaadin.ui.Layout;

import java.util.Date;

/**
 * @author devyatkin
 * @version $Id$
 */
public class DateFieldWrapper extends CustomField {

    private static final String DEFAULT_DATE_WIDTH = "100px";

    protected final WebDateField dateField;
    protected final Layout composition;

    public DateFieldWrapper(WebDateField dateField, Layout composition) {
        this.dateField = dateField;
        this.composition = composition;

        this.composition.setSizeUndefined();

        setSizeUndefined();
        setCompositionRoot(composition);
    }

    public WebDateField getCubaField() {
        return dateField;
    }

    @Override
    public void focus() {
        dateField.getDateField().focus();
    }

    @Override
    public Class<?> getType() {
        return Date.class;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        dateField.setEditable(!readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return !dateField.isEditable();
    }

    @Override
    public void setWidth(float width, int unit) {
        super.setWidth(width, unit);

        if (composition != null) {
            if (width < 0) {
                composition.setWidth(-1, UNITS_PIXELS);
                dateField.getDateField().setWidth(DEFAULT_DATE_WIDTH);
            } else {
                composition.setWidth(100, UNITS_PERCENTAGE);
                dateField.getDateField().setWidth("100%");
            }
        }
    }

    @Override
    public void setHeight(float height, int unit) {
        super.setHeight(height, unit);

        if (composition != null) {
            if (height < 0) {
                composition.setHeight(-1, UNITS_PIXELS);
            } else {
                composition.setHeight(100, UNITS_PERCENTAGE);
            }
        }
    }

    public void setCompositionReadOnly(boolean readonly) {
        super.setReadOnly(readonly);
    }

    @Override
    public Object getValue() {
        return dateField.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        dateField.setValue(newValue);
    }

    @Override
    protected boolean isEmpty() {
        return getValue() == null;
    }
}