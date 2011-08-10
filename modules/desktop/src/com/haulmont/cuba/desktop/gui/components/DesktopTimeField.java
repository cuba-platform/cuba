/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.data.Datasource;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTimeField extends DesktopAbstractField<JTextField> implements TimeField {

    private boolean showSeconds;

    public DesktopTimeField() {
        impl = new JTextField("TBD: TimeField");
    }

    @Override
    public boolean getShowSeconds() {
        return showSeconds;
    }

    @Override
    public void setShowSeconds(boolean showSeconds) {
        this.showSeconds = showSeconds;
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

    @Override
    public <T> T getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
    }
}
