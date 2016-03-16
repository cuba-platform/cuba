/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Collection;

/**
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
    public void addAction(Action action, int index) {
        pickerField.addAction(action, index);
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
        pickerField.checkDatasourceProperty(datasource, property);
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