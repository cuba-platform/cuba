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

package com.haulmont.cuba.web.widgets.client.scrollboxlayout;

import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.widgets.CubaScrollBoxLayout;
import com.haulmont.cuba.web.widgets.client.cssactionslayout.CubaCssActionsLayoutConnector;
import com.haulmont.cuba.web.widgets.client.cubascrollboxlayout.CubaScrollBoxLayoutServerRpc;
import com.haulmont.cuba.web.widgets.client.cubascrollboxlayout.CubaScrollBoxLayoutState;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.shared.ui.Connect;

@Connect(CubaScrollBoxLayout.class)
public class CubaScrollBoxLayoutConnector extends CubaCssActionsLayoutConnector implements SimpleManagedLayout {

    @Override
    public CubaScrollBoxLayoutState getState() {
        return (CubaScrollBoxLayoutState) super.getState();
    }

    @Override
    public CubaScrollBoxLayoutWidget getWidget() {
        return (CubaScrollBoxLayoutWidget) super.getWidget();
    }

    @Override
    public void layout() {
        CubaScrollBoxLayoutWidget widget = getWidget();

        widget.setScrollTop(getState().scrollTop);
        widget.setScrollLeft(getState().scrollLeft);

        widget.onScrollHandler = (scrollTop, scrollLeft) -> {
            if (getState().scrollChangeMode.equals(CubaScrollBoxLayoutState.DEFERRED_MODE)) {
                getRpcProxy(CubaScrollBoxLayoutServerRpc.class).setDeferredScroll(scrollTop, scrollLeft);
            } else {
                getRpcProxy(CubaScrollBoxLayoutServerRpc.class).setDelayedScroll(scrollTop, scrollLeft);
            }
        };
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("scrollTop")) {
            getWidget().setScrollTop(getState().scrollTop);
        }

        if (stateChangeEvent.hasPropertyChanged("scrollLeft")) {
            getWidget().setScrollLeft(getState().scrollLeft);
        }

        if (stateChangeEvent.hasPropertyChanged("scrollChangeMode")) {
            getWidget().setScrollChangeMode(getState().scrollChangeMode);
        }
    }
}