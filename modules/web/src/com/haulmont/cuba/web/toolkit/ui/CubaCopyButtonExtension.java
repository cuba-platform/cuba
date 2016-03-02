/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.button.CubaCopyButtonExtensionServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.button.CubaCopyButtonExtensionState;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Button;
import com.vaadin.util.ReflectTools;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * @author gorelov
 */
public class CubaCopyButtonExtension extends AbstractExtension {

    protected Button component;

    protected CubaCopyButtonExtension(Button button) {
        component = button;
        extend(component);

        registerRpc(new CubaCopyButtonExtensionServerRpc() {
            @Override
            public void copied(boolean success) {
                fireEvent(new CopyEvent(CubaCopyButtonExtension.this, success));
            }
        });
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

    public static class CopyEvent extends EventObject {
        private final boolean success;

        public CopyEvent(CubaCopyButtonExtension source, boolean success) {
            super(source);
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    public interface CopyListener extends Serializable {
        void copied(CopyEvent event);
    }

    private static Method COPY_METHOD = ReflectTools.findMethod(CopyListener.class, "copied", CopyEvent.class);

    public void addCopyListener(CopyListener listener) {
        addListener(CopyEvent.class, listener, COPY_METHOD);
    }

    public void removeCopyListener(CopyListener listener) {
        removeListener(CopyEvent.class, listener, COPY_METHOD);
    }
}
