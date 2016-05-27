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

package com.haulmont.cuba.web.toolkit.ui.client.checkbox;

import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.checkbox.CheckBoxConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = CubaCheckBox.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaCheckBoxConnector extends CheckBoxConnector {

    @Override
    public boolean delegateCaptionHandling() {
        return getWidget().captionManagedByLayout;
    }

    @Override
    public CubaCheckBoxWidget getWidget() {
        return (CubaCheckBoxWidget) super.getWidget();
    }

    @Override
    public CubaCheckBoxState getState() {
        return (CubaCheckBoxState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        getWidget().captionManagedByLayout = getState().captionManagedByLayout;

        super.onStateChanged(stateChangeEvent);
    }
}