/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaButtonField extends CustomField {

    protected Converter captionFormatter;

    public CubaButtonField() {
        setPrimaryStyleName("cuba-buttonfield");
    }

    @Override
    protected Component initContent() {
        Button button = new CubaButton();
        button.setStyleName(BaseTheme.BUTTON_LINK);
        return button;
    }

    @Override
    protected Button getContent() {
        return (Button) super.getContent();
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    @Override
    protected void setInternalValue(Object newValue) {
        //noinspection unchecked
        super.setInternalValue(newValue);

        if (captionFormatter == null) {
            getContent().setCaption(newValue == null ? "" : newValue.toString());
        } else {
            //noinspection unchecked
            String caption = (String) captionFormatter.convertToPresentation(newValue, String.class, getLocale());
            getContent().setCaption(caption);
        }
    }

    public Converter getCaptionFormatter() {
        return captionFormatter;
    }

    public void setCaptionFormatter(Converter captionFormatter) {
        this.captionFormatter = captionFormatter;
    }

    public void addClickListener(Button.ClickListener listener) {
        getContent().addClickListener(listener);
    }

    public void removeClickListener(Button.ClickListener listener) {
        getContent().removeClickListener(listener);
    }
}