/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonState;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * @author artamonov
 */
public class CubaButton extends com.vaadin.ui.Button {

    public CubaButton() {
    }

    public CubaButton(String caption) {
        super(caption);
    }

    public CubaButton(String caption, ClickListener listener) {
        super(caption, listener);
    }

    @Override
    protected CubaButtonState getState() {
        return (CubaButtonState) super.getState();
    }

    @Override
    protected CubaButtonState getState(boolean markAsDirty) {
        return (CubaButtonState) super.getState(markAsDirty);
    }

    @Override
    protected void fireClick(MouseEventDetails details) {
        try {
            super.fireClick(details);
        } finally {
            if (getState(false).useResponsePending) {
                getRpcProxy(CubaButtonClientRpc.class).onClickHandled();
            }
        }
    }

    public boolean isUseResponsePending() {
        return getState(false).useResponsePending;
    }

    public void setUseResponsePending(boolean useResponsePending) {
        if (isUseResponsePending() != useResponsePending) {
            getState().useResponsePending = useResponsePending;
        }
    }

    @Override
    public void setClickShortcut(int keyCode, int... modifiers) {
        if (clickShortcut != null) {
            removeShortcutListener(clickShortcut);
        }
        clickShortcut = new CubaClickShortcut(this, keyCode, modifiers);
        addShortcutListener(clickShortcut);
        getState().clickShortcutKeyCode = clickShortcut.getKeyCode();
    }

    protected static class CubaClickShortcut extends ClickShortcut {
        public CubaClickShortcut(Button button, int keyCode, int... modifiers) {
            super(button, keyCode, modifiers);
        }

        @Override
        public void handleAction(Object sender, Object target) {
            if (target instanceof Component) {
                Component targetTopLevelComponent = getTopLevelComponent((Component) target);
                Component buttonTopLevelComponent = getTopLevelComponent(button);

                if (targetTopLevelComponent == buttonTopLevelComponent) {
                    super.handleAction(sender, target);
                }
            }
        }

        protected Component getTopLevelComponent(Component component) {
            Component parent = component;
            while (parent != null && !(parent instanceof Window) && !(parent instanceof UI)) {
                parent = parent.getParent();
            }
            return parent;
        }
    }
}