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

import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.ui.VPopupCalendar;
import com.vaadin.ui.ClientWidget;

import java.util.Date;

@SuppressWarnings("serial")
@ClientWidget(VPopupCalendar.class)
public class
        DateField extends com.vaadin.ui.DateField {

    protected boolean closeWhenDateSelected = false;

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

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (isCloseWhenDateSelected()){
            target.addAttribute("closeWhenDateSelected", true);
        }
    }

    @Override
    protected Date handleUnparsableDateString(String dateString) throws ConversionException {
        requestRepaint();
        return null;
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
