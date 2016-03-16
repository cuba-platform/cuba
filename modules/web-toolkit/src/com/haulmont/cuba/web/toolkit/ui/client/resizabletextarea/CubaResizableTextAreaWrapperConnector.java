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

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextAreaWrapper;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.customfield.CustomFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaResizableTextAreaWrapper.class)
public class CubaResizableTextAreaWrapperConnector extends CustomFieldConnector {

    @Override
    protected void init() {
        super.init();

        getWidget().resizeHandler = new CubaResizableTextAreaWrapperWidget.ResizeHandler() {
            @Override
            public void handleResize() {
                getLayoutManager().setNeedsMeasure(CubaResizableTextAreaWrapperConnector.this);
            }

            @Override
            public void sizeChanged(String width, String height) {
                getRpcProxy(CubaResizableTextAreaWrapperServerRpc.class).sizeChanged(width, height);
            }

            @Override
            public void textChanged(String text) {
                getRpcProxy(CubaResizableTextAreaWrapperServerRpc.class).textChanged(text);
            }
        };
    }

    @Override
    public CubaResizableTextAreaWrapperState getState() {
        return (CubaResizableTextAreaWrapperState) super.getState();
    }

    @Override
    public CubaResizableTextAreaWrapperWidget getWidget() {
        return (CubaResizableTextAreaWrapperWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("resizable")) {
            getWidget().setResizable(getState().resizable);
        }

        if (stateChangeEvent.hasPropertyChanged("enabled")) {
            getWidget().setEnabled(isEnabled());
        }
    }
}
