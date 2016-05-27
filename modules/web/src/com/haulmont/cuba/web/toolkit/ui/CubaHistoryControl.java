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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.historycontrol.CubaHistoryControlServerRpc;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.ClientConnector;
import com.vaadin.ui.Layout;

public class CubaHistoryControl extends AbstractExtension {

    protected HistoryBackHandler handler;

    public CubaHistoryControl() {
        registerRpc((CubaHistoryControlServerRpc) () -> {
            if (handler != null) {
                handler.onHistoryBackPerformed();
            }
        });
    }

    public void extend(AbstractClientConnector target, HistoryBackHandler handler) {
        super.extend(target);

        this.handler = handler;
    }

    @Override
    protected Class<? extends ClientConnector> getSupportedParentType() {
        return Layout.class;
    }

    public interface HistoryBackHandler {

        void onHistoryBackPerformed();
    }
}