/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.BulkEditor;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.actions.BulkEditAction;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopBulkEditor extends DesktopButton implements BulkEditor {

    protected String exclude;
    protected WindowManager.OpenType openType = WindowManager.OpenType.DIALOG;
    protected BulkEditAction bulkEditAction;
    protected ListComponent listComponent;

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
}