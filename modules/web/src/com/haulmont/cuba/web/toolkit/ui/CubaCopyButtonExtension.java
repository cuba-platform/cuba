/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.button.CubaCopyButtonExtensionState;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Button;

/**
 * @author gorelov
 */
public class CubaCopyButtonExtension extends AbstractExtension {

    protected Button component;

    protected CubaCopyButtonExtension(Button button) {
        component = button;
        extend(component);
    }

    public static CubaCopyButtonExtension copyWith(Button button) {
        return new CubaCopyButtonExtension(button);
    }

    public static CubaCopyButtonExtension copyWith(Button button, String selector) {
        CubaCopyButtonExtension extension = new CubaCopyButtonExtension(button);
        extension.setCopyTargetSelector(selector);
        return extension;
    }

    @Override
    protected CubaCopyButtonExtensionState getState() {
        return (CubaCopyButtonExtensionState) super.getState();
    }

    @Override
    protected CubaCopyButtonExtensionState getState(boolean markAsDirty) {
        return (CubaCopyButtonExtensionState) super.getState(markAsDirty);
    }

    public String getCopyTargetSelector() {
        return getState(false).copyTargetSelector;
    }

    public void setCopyTargetSelector(String targetElementClass) {
        if (!equalValues(getState(false).copyTargetSelector, targetElementClass)) {
            getState().copyTargetSelector = targetElementClass;
        }
    }

    protected boolean equalValues(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }
}
