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

package com.haulmont.cuba.web.toolkit.ui.client.communication;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.haulmont.cuba.web.toolkit.ui.client.profiler.ScreenClientProfiler;
import com.vaadin.client.Profiler;
import com.vaadin.client.Util;
import com.vaadin.client.ValueMap;
import com.vaadin.client.communication.XhrConnection;

public class CubaXhrConnection extends XhrConnection {

    @Override
    protected XhrResponseHandler createResponseHandler() {
        return new XhrResponseHandler() {
            protected int serverTimeOnClient;

            @Override
            public void onResponseReceived(Request request, Response response) {
                int statusCode = response.getStatusCode();
                if (statusCode == 200) {
                    serverTimeOnClient = (int) Util.round(Profiler.getRelativeTimeMillis() - requestStartTime, 0);
                }
                super.onResponseReceived(request, response);
            }

            @Override
            protected void beforeHandlingMessage(ValueMap json) {
                super.beforeHandlingMessage(json);
                ScreenClientProfiler profiler = ScreenClientProfiler.getInstance();
                String profilerMarker = ScreenClientProfiler.getProfilerMarkerFromJson(json);
                if (profilerMarker != null) {
                    int serverTimeOnServer = ScreenClientProfiler.getServerTimeFromJson(json);
                    if (serverTimeOnServer > 0) {
                        profiler.registerServerTime(profilerMarker, serverTimeOnServer);
                        profiler.registerNetworkTime(profilerMarker, serverTimeOnClient - serverTimeOnServer);
                    } else {
                        profiler.registerServerTime(profilerMarker, serverTimeOnClient);
                    }
                    profiler.registerEventTs(profilerMarker, ScreenClientProfiler.getEventTsFromJson(json));
                }
            }
        };
    }
}
