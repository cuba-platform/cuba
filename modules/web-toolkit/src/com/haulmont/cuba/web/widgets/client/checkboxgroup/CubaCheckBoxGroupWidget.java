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

package com.haulmont.cuba.web.widgets.client.checkboxgroup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.VCheckBox;
import com.vaadin.client.ui.VCheckBoxGroup;
import elemental.json.JsonObject;

import java.util.List;

public class CubaCheckBoxGroupWidget extends VCheckBoxGroup implements KeyDownHandler {

    @Override
    protected void setOptionEnabled(VCheckBox checkBox, JsonObject item) {
        checkBox.setEnabled(isEnabled());
        checkBox.setStyleName(StyleConstants.DISABLED, !isEnabled());
    }

    @Override
    protected void setOptionReadOnly(VCheckBox checkBox, JsonObject item) {
        checkBox.setStyleName("v-readonly", isReadonly());
    }

    @Override
    public void buildOptions(List<JsonObject> items) {
        super.buildOptions(items);

        for (Widget widget : getWidget()) {
            if (widget instanceof VCheckBox) {
                ((VCheckBox) widget).addKeyDownHandler(this);
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (!isEnabled() || isReadonly()) {
            event.preventDefault();

            if (isReadonly()
                    && (BrowserInfo.get().isIE() || BrowserInfo.get().isEdge())) {
                // IE and Edge do not focus read-only checkbox on click
                Object checkBox = event.getSource();
                if (checkBox instanceof VCheckBox) {
                    ((VCheckBox) checkBox).setFocus(true);
                }
            }

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
}
