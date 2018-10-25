/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets.client.grid;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Focusable;
import com.vaadin.client.ui.VCustomField;

public class CubaEditorFieldWidget extends VCustomField {

    @Override
    public void focus() {
        if (focusDelegate != null) {
            focusDelegate.focus();
        } else {
            tryFocusChild();
        }
    }

    protected void tryFocusChild() {
        // Some components are based on Layout with several components within it.
        // In this case we can't set focus delegate, so we need to provide a workaround
        // to focus the first focusable component at least.
        Widget widget = getWidget();
        if (widget instanceof ComplexPanel) {
            ComplexPanel complexPanel = (ComplexPanel) widget;
            int widgetCount = complexPanel.getWidgetCount();
            for (int i = 0; i < widgetCount; i++) {
                Widget child = complexPanel.getWidget(i);
                if (child instanceof Focusable) {
                    ((Focusable) child).focus();
                    break;
                }
                if (child instanceof com.google.gwt.user.client.ui.Focusable) {
                    ((com.google.gwt.user.client.ui.Focusable) child).setFocus(true);
                    break;
                }
            }
        }
    }
}
