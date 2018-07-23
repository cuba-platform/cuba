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

package com.haulmont.cuba.web.widgets.client.searchselect;

import com.haulmont.cuba.web.widgets.CubaSearchSelect;
import com.vaadin.client.ui.combobox.ComboBoxConnector;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

@Connect(value = CubaSearchSelect.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaSearchSelectConnector extends ComboBoxConnector {

    @Override
    public CubaSearchSelectState getState() {
        return (CubaSearchSelectState) super.getState();
    }

    @Override
    public CubaSearchSelectWidget getWidget() {
        return (CubaSearchSelectWidget) super.getWidget();
    }

    // VAADIN8: gg, implement
    /*@Override
    protected void performSelection(String selectedKey) {
        super.performSelection(selectedKey);

        getWidget().updateEditState();
    }*/

    // VAADIN8: gg, implement
    /*@Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // update read only, cause tabIndex on the TextBox sets after updateReadOnly
        getWidget().updateReadOnly();
    }*/

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("tabIndex")) {
            getWidget().updateTabIndex(getState().tabIndex);
        }
    }

    // VAADIN8: gg, implement
    /*@Override
    protected void resetSelection() {
        if (getWidget().nullSelectionAllowed) {
            getWidget().currentSuggestion = null;
        }

        super.resetSelection();

        getWidget().updateEditState();
    }*/
}