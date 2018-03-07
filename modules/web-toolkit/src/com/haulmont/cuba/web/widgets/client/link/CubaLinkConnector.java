/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.link;

import com.haulmont.cuba.web.widgets.CubaLink;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.link.LinkConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaLink.class)
public class CubaLinkConnector extends LinkConnector {

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("rel")) {
            getWidget().setRel(getState().rel);
        }
    }

    @Override
    public CubaLinkState getState() {
        return (CubaLinkState) super.getState();
    }

    @Override
    public CubaLinkWidget getWidget() {
        return (CubaLinkWidget) super.getWidget();
    }
}