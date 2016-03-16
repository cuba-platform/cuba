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

package com.haulmont.cuba.web.toolkit.ui.client.optiongroup;

import com.haulmont.cuba.web.toolkit.ui.CubaOptionGroup;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.optiongroup.OptionGroupConnector;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaOptionGroup.class)
public class CubaOptionGroupConnector extends OptionGroupConnector {

    public static final String HORIZONTAL_ORIENTAION_STYLE = "horizontal";

    @Override
    public CubaOptionGroupState getState() {
        return (CubaOptionGroupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("orientation")) {
            if (getState().orientation == OptionGroupOrientation.VERTICAL)
                getWidget().removeStyleDependentName("horizontal");
            else
                getWidget().addStyleDependentName(HORIZONTAL_ORIENTAION_STYLE);
        }
    }
}