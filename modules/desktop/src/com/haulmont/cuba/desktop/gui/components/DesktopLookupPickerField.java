/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import javax.swing.*;
import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopLookupPickerField extends DesktopLookupField implements LookupPickerField {

    protected DesktopPickerField pickerField;

    public DesktopLookupPickerField() {
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
    public LookupAction addLookupAction() {
        LookupAction action = new LookupAction(this);
        addAction(action);
        return action;
    }

    @Override
    public ClearAction addClearAction() {
        ClearAction action = new ClearAction(this);
        addAction(action);
        return action;
    }

    @Override
    public OpenAction addOpenAction() {
        OpenAction action = new OpenAction(this);
        addAction(action);
        return action;
    }

    @Override
    public void addFieldListener(FieldListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFieldEditable(boolean editable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAction(Action action) {
        pickerField.addAction(action);
    }

    @Override
    public void removeAction(Action action) {
        pickerField.removeAction(action);
    }

    @Override
    public Collection<Action> getActions() {
        return pickerField.getActions();
    }

    @Override
    public Action getAction(String id) {
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
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        pickerField.setEnabled(enabled);
    }

    private class Picker extends com.haulmont.cuba.desktop.sys.vcl.Picker {
        @Override
        protected void initEditor() {
            // put LookupField into PickerField composition
            editor = DesktopLookupPickerField.super.getComposition();
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