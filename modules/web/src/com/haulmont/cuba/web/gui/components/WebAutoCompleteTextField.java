/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.AutoCompleteTextField;
import com.haulmont.cuba.gui.components.Component;

/**
 * @author abramov
 * @version $Id$
 */
public class WebAutoCompleteTextField
    extends
        WebAbstractTextField<com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField>
    implements
        AutoCompleteTextField, Component.Wrapper {

    protected boolean trimming = true;

    @Override
    protected com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField createTextFieldImpl() {
        return new com.haulmont.cuba.web.toolkit.ui.AutoCompleteTextField();
    }

    @Override
    public void setSuggester(Suggester suggester) {
        component.setSuggester(suggester);
    }

    @Override
    public AutoCompleteSupport getAutoCompleteSupport() {
        return component;
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
    public int getRows() {
        return component.getRows();
    }

    @Override
    public void setRows(int rows) {
        component.setRows(rows);
    }

    @Override
    public int getColumns() {
        return component.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        component.setColumns(columns);
    }

    @Override
    public boolean isTrimming() {
        return trimming;
    }

    @Override
    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }
}