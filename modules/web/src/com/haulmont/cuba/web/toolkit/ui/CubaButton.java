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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonState;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

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