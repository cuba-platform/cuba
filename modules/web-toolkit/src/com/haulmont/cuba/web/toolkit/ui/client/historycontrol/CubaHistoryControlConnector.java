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

package com.haulmont.cuba.web.toolkit.ui.client.historycontrol;

import com.haulmont.cuba.web.toolkit.ui.CubaHistoryControl;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.VNotification;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(CubaHistoryControl.class)
public class CubaHistoryControlConnector extends AbstractExtensionConnector {

    protected ServerConnector target;
    protected HistoryGwtApi historyApi;

    @Override
    protected void extend(ServerConnector target) {
        this.target = target;
        this.historyApi = new HistoryGwtApi() {
            @Override
            protected void onHistoryBackPerformed() {
                handleHistoryBackAction();
            }

            @Override
            protected boolean isEnabled() {
                return isAttached();
            }
        };
    }

    public void handleHistoryBackAction() {
        // handle notifications
        if (VNotification.getLastNotification() != null) {
            VNotification.getLastNotification().hide();
        } else {
            getRpcProxy(CubaHistoryControlServerRpc.class).onHistoryBackPerformed();
        }
    }

    protected boolean isAttached() {
        AbstractComponentConnector connectorTarget = (AbstractComponentConnector) target;
        return connectorTarget != null
                && connectorTarget.getWidget() != null
                && connectorTarget.getWidget().isAttached();
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        this.historyApi.disable();
    }
}