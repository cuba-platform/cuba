/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.listselect;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.vaadin.client.WidgetUtil;
import com.vaadin.v7.client.ui.VListSelect;

import java.util.function.Consumer;

public class CubaListSelectWidget extends VListSelect {

    protected Consumer<Integer> doubleClickListener;

    public CubaListSelectWidget() {
        getOptionsContainer().addDoubleClickHandler(event -> {
            if (!isEnabled() || isReadonly()) {
                return;
            }

            Element element = WidgetUtil.getElementUnderMouse(event.getNativeEvent());

            if (OptionElement.is(element)) {
                doubleClickListener.accept(((OptionElement) element).getIndex());
            }
        });
    }

    @Override
    protected void updateEnabledState() {
        select.setEnabled(isEnabled());
        select.setStyleName("v-readonly", isReadonly());
    }

    @Override
    public void onClick(ClickEvent event) {
        if (!isEnabled() || isReadonly()) {
            return;
        }

        super.onClick(event);
    }

    @Override
    public void onChange(ChangeEvent event) {
        if (!isEnabled() || isReadonly()) {
            return;
        }

        super.onChange(event);
    }
}
