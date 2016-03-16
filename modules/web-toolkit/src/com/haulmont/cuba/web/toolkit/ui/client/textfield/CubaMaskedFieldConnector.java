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

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.haulmont.cuba.web.toolkit.ui.CubaMaskedTextField;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaMaskedTextField.class)
public class CubaMaskedFieldConnector extends TextFieldConnector {

    @Override
    public CubaMaskedTextFieldState getState() {
        return (CubaMaskedTextFieldState) super.getState();
    }

    @Override
    public CubaMaskedFieldWidget getWidget() {
        return (CubaMaskedFieldWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setMask(getState().mask);
        getWidget().setMaskedMode(getState().maskedMode);
        getWidget().setSendNullRepresentation(getState().sendNullRepresentation);
    }
}