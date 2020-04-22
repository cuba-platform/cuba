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

package com.haulmont.cuba.web.widgets.client.split;

import com.google.gwt.core.client.Scheduler;
import com.haulmont.cuba.web.widgets.CubaVerticalSplitPanel;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.PostLayoutListener;
import com.vaadin.client.ui.splitpanel.VerticalSplitPanelConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = CubaVerticalSplitPanel.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaVerticalSplitPanelConnector extends VerticalSplitPanelConnector
        implements PostLayoutListener {

    protected boolean updateLayout = false;

    @Override
    protected void init() {
        super.init();

        getWidget().beforeDockPositionHandler =
                position -> getRpcProxy(CubaDockableSplitPanelServerRpc.class).setBeforeDockPosition(position);
    }

    @Override
    public CubaVerticalSplitPanelState getState() {
        return (CubaVerticalSplitPanelState) super.getState();
    }

    @Override
    public CubaVerticalSplitPanelWidget getWidget() {
        return (CubaVerticalSplitPanelWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("dockable")) {
            getWidget().setDockable(getState().dockable);
        }
        if (stateChangeEvent.hasPropertyChanged("dockMode")) {
            getWidget().setDockMode(getState().dockMode);
        }
        if (stateChangeEvent.hasPropertyChanged("defaultPosition")) {
            getWidget().defaultPosition = getState().defaultPosition;
        }
        if (stateChangeEvent.hasPropertyChanged("beforeDockPosition")) {
            getWidget().beforeDockPosition = getState().beforeDockPosition;
        }

        updateLayout = true;
    }

    @Override
    public void postLayout() {
        // Have to re-layout after parent layout expand hack applied
        // to avoid split position glitch.
        if (updateLayout) {
            Scheduler.get().scheduleFinally(this::layout);
            updateLayout = false;
        }
    }
}
