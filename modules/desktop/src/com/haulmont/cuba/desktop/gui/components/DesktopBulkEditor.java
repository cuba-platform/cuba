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

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.BulkEditor;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.actions.BulkEditAction;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 */
public class DesktopBulkEditor extends DesktopButton implements BulkEditor {

    protected String exclude;
    protected WindowManager.OpenType openType = WindowManager.OpenType.DIALOG;
    protected BulkEditAction bulkEditAction;
    protected ListComponent listComponent;
    protected Map<String, Field.Validator> fieldValidators;
    protected List<Field.Validator> modelValidators;

    public DesktopBulkEditor() {
        setCaption(null);
    }

    @Override
    public WindowManager.OpenType getOpenType() {
        return openType;
    }

    @Override
    public void setOpenType(WindowManager.OpenType openType) {
        this.openType = openType;
        if (bulkEditAction != null) {
            bulkEditAction.setOpenType(openType);
        }
    }

    @Override
    public String getExcludePropertiesRegex() {
        return exclude;
    }

    @Override
    public void setExcludePropertiesRegex(String exclude) {
        this.exclude = exclude;
        if (bulkEditAction != null) {
            bulkEditAction.setExcludePropertyRegex(exclude);
        }
    }

    @Override
    public ListComponent getListComponent() {
        return listComponent;
    }

    @Override
    public void setListComponent(ListComponent listComponent) {
        this.listComponent = listComponent;

        if (listComponent != null) {
            String caption = getCaption();
            String description = getDescription();
            String icon = getIcon();

            boolean enabled = isEnabled();
            boolean visible = isVisible();

            bulkEditAction = new BulkEditAction(listComponent);
            setAction(bulkEditAction);

            if (openType != null) {
                bulkEditAction.setOpenType(openType);
            }

            if (exclude != null) {
                bulkEditAction.setExcludePropertyRegex(exclude);
            }

            if (fieldValidators != null) {
                bulkEditAction.setFieldValidators(fieldValidators);
            }

            if (modelValidators != null) {
                bulkEditAction.setModelValidators(modelValidators);
            }

            if (caption != null) {
                bulkEditAction.setCaption(caption);
            }
            if (description != null) {
                bulkEditAction.setDescription(description);
            }
            if (icon != null) {
                bulkEditAction.setIcon(icon);
            }
            bulkEditAction.setEnabled(enabled);
            bulkEditAction.setVisible(visible);

            listComponent.addAction(bulkEditAction);
        }
    }

    @Override
    public Map<String, Field.Validator> getFieldValidators() {
        return fieldValidators == null ? null : Collections.unmodifiableMap(fieldValidators);
    }

    @Override
    public void setFieldValidators(Map<String, Field.Validator> fieldValidators) {
        this.fieldValidators = fieldValidators;
        if (bulkEditAction != null) {
            bulkEditAction.setFieldValidators(fieldValidators);
        }
    }

    @Override
    public List<Field.Validator> getModelValidators() {
        return modelValidators == null ? null : Collections.unmodifiableList(modelValidators);
    }

    @Override
    public void setModelValidators(List<Field.Validator> modelValidators) {
        this.modelValidators = modelValidators;
        if (bulkEditAction != null) {
            bulkEditAction.setModelValidators(modelValidators);
        }
    }
}