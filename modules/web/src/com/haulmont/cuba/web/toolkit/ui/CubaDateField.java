/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.datefield.CubaDateFieldState;
import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaDateField extends com.vaadin.ui.DateField {

    private static final long serialVersionUID = 6017244766993879882L;

    protected String lastInvalidDateString;

    protected String dateString;

    private final Date MARKER_DATE = new Date(0);

    protected Date prevValue;

    @Override
    protected CubaDateFieldState getState() {
        return (CubaDateFieldState) super.getState();
    }

    @Override
    protected CubaDateFieldState getState(boolean markAsDirty) {
        return (CubaDateFieldState) super.getState(markAsDirty);
    }

    @Override
    protected void setValue(Date newValue, boolean repaintIsNotNeeded) throws Converter.ConversionException {
        if (newValue == MARKER_DATE)
            super.setValue(prevValue, true);
        else {
            prevValue = newValue;
            super.setValue(newValue, repaintIsNotNeeded);
        }
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        lastInvalidDateString = (String) variables.get("lastInvalidDateString");
        dateString = (String) variables.get("dateString");
        super.changeVariables(source, variables);
    }

    @Override
    public void setDateFormat(String dateFormat) {
        super.setDateFormat(dateFormat);
        getState().dateMask = StringUtils.replaceChars(dateFormat, "dDMYy", "#####");
        markAsDirty();
    }

    @Override
    protected Date handleUnparsableDateString(String dateString) throws Converter.ConversionException {
        if (ObjectUtils.equals(dateString, StringUtils.replaceChars(getState(false).dateMask, "#U", "__"))) {
            return null;
        }

        markAsDirty();
        return MARKER_DATE;
    }
}