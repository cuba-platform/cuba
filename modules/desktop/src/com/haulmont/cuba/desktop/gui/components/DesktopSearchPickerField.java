/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.SearchPickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import javax.swing.*;
import java.util.Collection;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopSearchPickerField extends DesktopSearchField implements SearchPickerField {

    protected DesktopPickerField pickerField;

    public DesktopSearchPickerField() {
        pickerField = new DesktopPickerField(new Picker());
    }

    @Override
    public JComponent getComposition() {
        return pickerField.getComposition();
    }

    @Override
    public MetaClass getMetaClass() {
        return pickerField.getMetaClass();
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        pickerField.setMetaClass(metaClass);
    }

    @Override
    public PickerField.LookupAction addLookupAction() {
        PickerField.LookupAction action = new PickerField.LookupAction(this);
        addAction(action);
        return action;
    }

    @Override
    public PickerField.ClearAction addClearAction() {
        PickerField.ClearAction action = new PickerField.ClearAction(this);
        addAction(action);
        return action;
    }

    @Override
    public PickerField.OpenAction addOpenAction() {
        PickerField.OpenAction action = new PickerField.OpenAction(this);
        addAction(action);
        return action;
    }

    @Override
    public void addFieldListener(PickerField.FieldListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFieldEditable(boolean editable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAction(com.haulmont.cuba.gui.components.Action action) {
        pickerField.addAction(action);
    }

    @Override
    public void removeAction(com.haulmont.cuba.gui.components.Action action) {
        pickerField.removeAction(action);
    }

    @Override
    public void removeAction(String id) {
        pickerField.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        pickerField.removeAllActions();
    }

    @Override
    public Collection<com.haulmont.cuba.gui.components.Action> getActions() {
        return pickerField.getActions();
    }

    @Override
    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        return pickerField.getAction(id);
    }

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);
        pickerField.setFrame(frame);
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);
        pickerField.setDatasource(datasource, property);
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        super.setOptionsDatasource(datasource);
        if (pickerField.getMetaClass() == null && datasource != null) {
            pickerField.setMetaClass(datasource.getMetaClass());
        }
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        pickerField.setEditable(editable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        pickerField.setEnabled(enabled);
    }

    private class Picker extends com.haulmont.cuba.desktop.sys.vcl.Picker {
        @Override
        protected void initEditor() {
            // put LookupField into PickerField composition
            editor = DesktopSearchPickerField.super.getComposition();
        }

        @Override
        public JComponent getInputField() {
            return getInputComponent();
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object value) {
        }
    }
}