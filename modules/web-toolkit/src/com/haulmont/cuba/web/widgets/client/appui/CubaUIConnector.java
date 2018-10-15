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

package com.haulmont.cuba.web.widgets.client.appui;

import com.haulmont.cuba.web.widgets.CubaUI;
import com.haulmont.cuba.web.widgets.client.button.CubaButtonConnector;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ApplicationConnection.RemoveMethodInvocationCallback;
import com.vaadin.client.ConnectorMap;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.VNotification;
import com.vaadin.client.ui.ui.UIConnector;
import com.vaadin.shared.communication.LegacyChangeVariablesInvocation;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.button.ButtonServerRpc;
import com.vaadin.shared.ui.tabsheet.TabsheetServerRpc;

@Connect(CubaUI.class)
public class CubaUIConnector extends UIConnector {

    public CubaUIConnector() {
        VNotification.setRelativeZIndex(true);

        //noinspection Convert2Lambda
        registerRpc(CubaUIClientRpc.class, new CubaUIClientRpc() {
            @Override
            public void discardAccumulatedEvents() {
                // silent time
                ValidationErrorHolder.onValidationError();

                // vaadin8 - get rid of this mechanism
                ApplicationConnection.MethodInvocationFilter filter = mi -> {
                    // use blacklist of invocations
                    // do not discard all

                    // button click
                    if (ButtonServerRpc.class.getName().equals(mi.getInterfaceName())
                            && "click".equals(mi.getMethodName())) {
                        return true;
                    }

                    // tabsheet close
                    if (TabsheetServerRpc.class.getName().equals(mi.getInterfaceName())
                            && "closeTab".equals(mi.getMethodName())) {
                        return true;
                    }

                    // shortcuts && window close
                    //noinspection RedundantIfStatement
                    if (mi instanceof LegacyChangeVariablesInvocation) {
                        LegacyChangeVariablesInvocation invocation = (LegacyChangeVariablesInvocation) mi;
                        if (invocation.getVariableChanges().containsKey("action")
                                || invocation.getVariableChanges().containsKey("actiontarget")
                                || invocation.getVariableChanges().containsKey("close")) {
                            return true;
                        }
                    }

                    return false;
                };

                RemoveMethodInvocationCallback callback = mi -> {
                    ConnectorMap connectorMap = getConnection().getConnectorMap();
                    ServerConnector connector = connectorMap.getConnector(mi.getConnectorId());
                    if (connector instanceof CubaButtonConnector) {
                        ((CubaButtonConnector) connector).stopResponsePending();
                    }
                };

                getConnection().removePendingInvocationsAndBursts(filter, callback);
            }
        });
    }
}