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
 */

package com.haulmont.cuba.web.toolkit.ui.client.profiler;


import com.haulmont.cuba.web.toolkit.ui.ScreenClientProfilerAgent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = ScreenClientProfilerAgent.class, loadStyle = Connect.LoadStyle.EAGER)
public class ScreenClientProfilerConnector extends AbstractExtensionConnector {

    protected ScreenClientProfilerServerRpc rpc = RpcProxy.create(ScreenClientProfilerServerRpc.class, this);

    public ScreenClientProfilerConnector() {
        ScreenClientProfiler.getInstance().addListener(new ScreenClientProfiler.FlushEventsListener() {
            @Override
            public void flush(ScreenProfilerClientEvent[] events) {
                rpc.flushEvents(events);
            }
        });
    }

    @Override
    protected void extend(ServerConnector target) {
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        ScreenClientProfiler clientProfiler = ScreenClientProfiler.getInstance();
        clientProfiler.setFlushEventsCount(getState().flushEventsCount);
        clientProfiler.setFlushTimeout(getState().flushTimeout);
    }

    @Override
    public ScreenClientProfilerState getState() {
        return (ScreenClientProfilerState) super.getState();
    }
}
