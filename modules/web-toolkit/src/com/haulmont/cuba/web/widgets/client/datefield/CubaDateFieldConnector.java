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

package com.haulmont.cuba.web.widgets.client.datefield;

import com.haulmont.cuba.web.widgets.CubaDateField;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.datefield.PopupDateFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaDateField.class)
public class CubaDateFieldConnector extends PopupDateFieldConnector {

    @Override
    public CubaDateFieldWidget getWidget() {
        return (CubaDateFieldWidget) super.getWidget();
    }

    @Override
    public CubaDateFieldState getState() {
        return (CubaDateFieldState) super.getState();
    }

    @Override
    public boolean delegateCaptionHandling() {
        return getState().captionManagedByLayout;
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        getWidget().getImpl().setMask(getState().dateMask);
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("tabIndex")) {
            getWidget().updateTabIndex(getState().tabIndex);
        }

        if (stateChangeEvent.hasPropertyChanged("autofill")) {
            getWidget().setAutofill(getState().autofill);
        }

        if (stateChangeEvent.hasPropertyChanged("rangeStart")) {
            getWidget().setDateRangeStart(getState().rangeStart);
        }

        if (stateChangeEvent.hasPropertyChanged("rangeEnd")) {
            getWidget().setDateRangeEnd(getState().rangeEnd);
        }
    }
}