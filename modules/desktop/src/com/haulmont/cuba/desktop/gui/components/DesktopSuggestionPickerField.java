/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SearchPickerField;
import com.haulmont.cuba.gui.components.SuggestionPickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Collection;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopSuggestionPickerField extends DesktopSuggestionField implements SuggestionPickerField {

    protected DesktopPickerField pickerField;

    public DesktopSuggestionPickerField() {
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
    public void addAction(com.haulmont.cuba.gui.components.Action action) {
        pickerField.addAction(action);
    }

    @Override
    public void removeAction(@Nullable com.haulmont.cuba.gui.components.Action action) {
        pickerField.removeAction(action);
    }

    @Override
    public void removeAction(@Nullable String id) {
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
    @Nullable
    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        return pickerField.getAction(id);
    }

    @Nonnull
    @Override
    public com.haulmont.cuba.gui.components.Action getActionNN(String id) {
        com.haulmont.cuba.gui.components.Action action = getAction(id);
        if (action == null) {
            throw new IllegalStateException("Unable to find action with id " + id);
        }
        return action;
    }

    @Override
    public void setFrame(Frame frame) {
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
    public void updateEnabled() {
        super.updateEnabled();

        pickerField.setParentEnabled(isEnabledWithParent());
    }

    private class Picker extends com.haulmont.cuba.desktop.sys.vcl.Picker {
        @Override
        protected void initEditor() {
            // put LookupField into PickerField composition
            editor = DesktopSuggestionPickerField.super.getComposition();
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