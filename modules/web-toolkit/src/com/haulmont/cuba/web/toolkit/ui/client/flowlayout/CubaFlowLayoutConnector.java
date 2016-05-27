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

package com.haulmont.cuba.web.toolkit.ui.client.flowlayout;

import com.haulmont.cuba.web.toolkit.ui.CubaFlowLayout;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.MarginInfo;

@Connect(CubaFlowLayout.class)
public class CubaFlowLayoutConnector extends CssLayoutConnector {

    @Override
    public CubaFlowLayoutState getState() {
        return (CubaFlowLayoutState) super.getState();
    }

    @Override
    public CubaFlowLayoutWidget getWidget() {
        return (CubaFlowLayoutWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setMargin(new MarginInfo(getState().marginsBitmask));
        getWidget().setSpacing(getState().spacing);
    }
}