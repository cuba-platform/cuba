/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.11.2009 11:28:58
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VMaskedPopupCalendar;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;

@SuppressWarnings("serial")
@ClientWidget(VMaskedPopupCalendar.class)
public class DateField extends com.vaadin.ui.DateField {

    protected boolean closeWhenDateSelected = false;

    protected String mask;

    protected String lastInvalidDateString;

    protected String dateString;

    private final Date MARKER_DATE = new Date(0);

    protected Object prevValue;

    public DateField() {
    }

    public DateField(String caption) {
        super(caption);
    }

    public DateField(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    public DateField(Property dataSource) throws IllegalArgumentException {
        super(dataSource);
    }

    public DateField(String caption, Date value) {
        super(caption, value);
    }

    protected void setValue(Object newValue, boolean repaintIsNotNeeded) {
        if (newValue == MARKER_DATE) {
            if (ObjectUtils.equals(lastInvalidDateString, StringUtils.replaceChars(mask, "#U", "__"))) {
                newValue = null;
            } else {
                super.setValue(prevValue);
                throw new Validator.InvalidValueException("Unable to parse date");
            }
        }
        prevValue = newValue;
        super.setValue(newValue, repaintIsNotNeeded);
    }

    public void changeVariables(Object source, Map<String, Object> variables) {
        lastInvalidDateString = (String) variables.get("lastInvalidDateString");
        dateString = (String) variables.get("dateString");
        super.changeVariables(source, variables);
    }

    public void setDateFormat(String dateFormat) {
        super.setDateFormat(dateFormat);
        setMask(StringUtils.replaceChars(dateFormat, "dDMYy", "#####"));
        requestRepaint();
    }

    public void setMask(String mask) {
        this.mask = mask;
        requestRepaint();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (isCloseWhenDateSelected()) {
            target.addAttribute("closeWhenDateSelected", true);
        }
        if (mask != null) {
            target.addAttribute("mask", mask);
        }
    }

    @Override
    protected Date handleUnparsableDateString(String dateString) throws ConversionException {
        requestRepaint();
        return MARKER_DATE;
    }

    @Override
    protected boolean isEmpty() {
        return getValue() == null;
    }
        

    public boolean isCloseWhenDateSelected() {
        return closeWhenDateSelected;
    }

    public void setCloseWhenDateSelected(boolean closeWhenDateSelected) {
        this.closeWhenDateSelected = closeWhenDateSelected;
    }
}
