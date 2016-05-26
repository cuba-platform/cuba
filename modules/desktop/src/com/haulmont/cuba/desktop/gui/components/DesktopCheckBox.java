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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;

public class DesktopCheckBox extends DesktopAbstractField<JCheckBox> implements CheckBox {
    protected Datasource datasource;
    protected boolean updatingInstance;
    protected Object prevValue;

    protected boolean editable = true;

    public DesktopCheckBox() {
        impl = new JCheckBox();
        impl.addActionListener(e -> {
            updateInstance();
            fireChangeListeners(impl.isSelected());
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        return (T) (Boolean) impl.isSelected();
    }

    @Override
    public void setValue(Object value) {
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (value == null) {
            value = false;
        }

        if (!ObjectUtils.equals(prevValue, value)) {
            updateComponent(value);
            updateInstance();
            fireChangeListeners(value);
        }
    }

    private void updateComponent(Object value) {
        impl.setSelected(value != null && BooleanUtils.isTrue((Boolean) value));
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        if (datasource == null) {
            setValue(null);
            return;
        }
        resolveMetaPropertyPath(datasource.getMetaClass(), property);

        datasource.addItemChangeListener(e -> {
            if (updatingInstance)
                return;
            Boolean value = InstanceUtils.getValueEx(e.getItem(), metaPropertyPath.getPath());
            if (value == null) {
                value = false;
            }

            updateComponent(value);
            fireChangeListeners(value);
        });

        datasource.addItemPropertyChangeListener(e -> {
            if (updatingInstance) {
                return;
            }

            if (e.getProperty().equals(metaPropertyPath.toString())) {
                Object value = e.getValue();
                if (e.getValue() == null) {
                    value = false;
                }

                updateComponent(value);
                fireChangeListeners(value);
            }
        });

        if (datasource.getItemIfValid() != null) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());

            if (!ObjectUtils.equals(prevValue, newValue)) {
                updateComponent(newValue);
                fireChangeListeners(newValue);
            }
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }
    }

    @Override
    public String getCaption() {
        return impl.getText();
    }

    @Override
    public void setCaption(String caption) {
        impl.setText(caption);
    }

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;

        updateEnabled();
    }

    @Override
    public void updateEnabled() {
        impl.setEnabled(editable && isEnabledWithParent());
    }

    protected void updateInstance() {
        if (updatingInstance)
            return;

        updatingInstance = true;
        try {
            if ((datasource != null) && (metaPropertyPath != null)) {
                boolean value = impl.isSelected();
                if (datasource.getItem() != null) {
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
                }
            }
        } finally {
            updatingInstance = false;
        }
    }

    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }
}