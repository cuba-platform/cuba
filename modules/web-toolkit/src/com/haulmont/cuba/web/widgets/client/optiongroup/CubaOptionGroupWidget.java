/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.optiongroup;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.UIDL;
import com.vaadin.v7.client.ui.VOptionGroup;

public class CubaOptionGroupWidget extends VOptionGroup implements KeyDownHandler, ValueChangeHandler<Boolean> {

    @Override
    public void buildOptions(UIDL uidl) {
        super.buildOptions(uidl);

        for (Widget widget : panel) {
            if (widget instanceof RadioButton) {
                ((RadioButton) widget).addKeyDownHandler(this);
                ((RadioButton) widget).addValueChangeHandler(this);
            }
        }

        updateEnabledState();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (!isEnabled() || isReadonly()) {
            event.preventDefault();
            return;
        }

        super.onClick(event);
    }


    @Override
    public void onKeyDown(KeyDownEvent event) {
        if ((!isEnabled() || isReadonly())
                && event.getNativeKeyCode() != KeyCodes.KEY_TAB) {
            event.preventDefault();
        }
    }

    @Override
    protected void updateEnabledState() {
        for (Widget w : panel) {
            if (w instanceof HasEnabled) {
                HasEnabled hasEnabled = (HasEnabled) w;
                hasEnabled.setEnabled(isEnabled());

                w.setStyleName("v-readonly", isReadonly());
            }
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (isReadonly()
                && (BrowserInfo.get().isIE() || BrowserInfo.get().isEdge())) {
            // IE and Edge reset radioButton checked when clicking on another radioButton
            updateItemsSelection();
        }
    }

    protected void updateItemsSelection() {
        for (Widget w : panel) {
            if (w instanceof RadioButton) {
                Element input = w.getElement().getFirstChildElement();
                if (input instanceof InputElement) {
                    boolean checked = ((InputElement) input).isDefaultChecked();
                    ((InputElement) input).setChecked(checked);
                }
            }
        }
    }
}
