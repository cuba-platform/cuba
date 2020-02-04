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

package com.haulmont.cuba.web.widgets.client.radiobuttongroup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.VRadioButtonGroup;
import elemental.json.JsonObject;

import java.util.List;

public class CubaRadioButtonGroupWidget extends VRadioButtonGroup implements KeyDownHandler, ValueChangeHandler<Boolean> {

    @Override
    public void buildOptions(List<JsonObject> items) {
        super.buildOptions(items);

        for (Widget widget : getWidget()) {
            if (widget instanceof RadioButton) {
                ((RadioButton) widget).addKeyDownHandler(this);
                ((RadioButton) widget).addValueChangeHandler(this);
            }
        }
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
    protected void updateItemEnabled(RadioButton radioButton, boolean value) {
        boolean enabled = isEnabled();
        radioButton.setEnabled(enabled);
        radioButton.setStyleName(StyleConstants.DISABLED, !enabled);

        radioButton.setStyleName("v-readonly", isReadonly());
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
        for (Widget widget : getWidget()) {
            if (widget instanceof RadioButton) {
                boolean checked = widget.getElement().hasClassName(CLASSNAME_OPTION_SELECTED);
                updateItemSelection((RadioButton) widget, checked);
            }
        }
    }
}
