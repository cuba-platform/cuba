/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.BulkEditor;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.actions.BulkEditAction;

import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebBulkEditor extends WebButton implements BulkEditor {

    protected String exclude;
    protected WindowManager.OpenType openType = WindowManager.OpenType.DIALOG;
    protected BulkEditAction bulkEditAction;
    protected ListComponent listComponent;
    protected Map<String, Field.Validator> fieldValidators;
    protected List<Field.Validator> moduleValidators;

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

            if (moduleValidators != null) {
                bulkEditAction.setModuleValidators(moduleValidators);
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
    public List<Field.Validator> getModuleValidators() {
        return moduleValidators == null ? null : Collections.unmodifiableList(moduleValidators);
    }

    @Override
    public void setModuleValidators(List<Field.Validator> moduleValidators) {
        this.moduleValidators = moduleValidators;
        if (bulkEditAction != null) {
            bulkEditAction.setModuleValidators(moduleValidators);
        }
    }
}