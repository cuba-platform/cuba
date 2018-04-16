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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.MaskedField;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.widgets.CubaMaskedTextField;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;

public class WebMaskedField extends WebV8AbstractField<CubaMaskedTextField, String, String> implements MaskedField {

    protected ShortcutListener enterShortcutListener;

    public WebMaskedField() {
        this.component = createTextFieldImpl();

        attachValueChangeListener(component);
    }

    @Override
    public void setMask(String mask) {
        component.setMask(mask);
    }

    @Override
    public String getMask() {
        return component.getMask();
    }

    @Override
    public void setValueMode(ValueMode mode) {
        component.setMaskedMode(mode == ValueMode.MASKED);
    }

    @Override
    public ValueMode getValueMode() {
        return component.isMaskedMode() ? ValueMode.MASKED : ValueMode.CLEAR;
    }

    @Override
    public boolean isSendNullRepresentation() {
        return component.isSendNullRepresentation();
    }

    @Override
    public void setSendNullRepresentation(boolean sendNullRepresentation) {
        component.setSendNullRepresentation(sendNullRepresentation);
    }

    @Override
    public String getRawValue() {
        return component.getValue();
    }

//    vaadin8
//    @Override
    protected CubaMaskedTextField createTextFieldImpl() {
        return new CubaMaskedTextField();
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }

    @Override
    public void selectAll() {
        component.selectAll();
    }

    @Override
    public void setSelectionRange(int pos, int length) {
//        vaadin8
//        component.setSelectionRange(pos, length);
    }

    @Override
    public void addEnterPressListener(EnterPressListener listener) {
        getEventRouter().addListener(EnterPressListener.class, listener);

        if (enterShortcutListener == null) {
            enterShortcutListener = new ShortcutListenerDelegate("enter", KeyCode.ENTER, null)
                    .withHandler((sender, target) -> {
                        EnterPressEvent event = new EnterPressEvent(WebMaskedField.this);
                        getEventRouter().fireEvent(EnterPressListener.class, EnterPressListener::enterPressed, event);
                    });
            component.addShortcutListener(enterShortcutListener);
        }
    }

    @Override
    public void removeEnterPressListener(EnterPressListener listener) {
        getEventRouter().removeListener(EnterPressListener.class, listener);

        if (enterShortcutListener != null && !getEventRouter().hasListeners(EnterPressListener.class)) {
            component.removeShortcutListener(enterShortcutListener);
        }
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        // vaadin8
    }

    @Override
    public void discard() {
        // vaadin8
    }

    @Override
    public boolean isBuffered() {
        // vaadin8
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        // vaadin8
    }

    @Override
    public boolean isModified() {
        // vaadin8
        return false;
    }
}