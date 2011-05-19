/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
// TODO DesktopDateField
public class DesktopDateField
    extends DesktopAbstractComponent<JPanel>
    implements DateField
{
    public DesktopDateField() {
        impl = new JPanel(new java.awt.FlowLayout());
        impl.setBorder(BorderFactory.createLineBorder(java.awt.Color.gray));
        impl.add(new JLabel("TODO: dateField"));
    }

    @Override
    public Resolution getResolution() {
        return null;
    }

    @Override
    public void setResolution(Resolution resolution) {
    }

    @Override
    public String getDateFormat() {
        return null;
    }

    @Override
    public void setDateFormat(String dateFormat) {
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public void setRequired(boolean required) {
    }

    @Override
    public void setRequiredMessage(String msg) {
    }

    @Override
    public <T> T getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public void addListener(ValueListener listener) {
    }

    @Override
    public void removeListener(ValueListener listener) {
    }

    @Override
    public void addValidator(Validator validator) {
    }

    @Override
    public void removeValidator(Validator validator) {
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
    public Datasource getDatasource() {
        return null;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }
}
