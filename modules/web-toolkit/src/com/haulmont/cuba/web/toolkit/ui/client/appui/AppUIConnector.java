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

package com.haulmont.cuba.web.toolkit.ui.client.appui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.toolkit.ui.client.button.CubaButtonConnector;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ConnectorMap;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.NotificationDelegate;
import com.vaadin.client.ui.VNotification;
import com.vaadin.client.ui.ui.UIConnector;
import com.vaadin.shared.communication.LegacyChangeVariablesInvocation;
import com.vaadin.shared.communication.MethodInvocation;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.button.ButtonServerRpc;
import com.vaadin.shared.ui.tabsheet.TabsheetServerRpc;

/**
 */
@Connect(AppUI.class)
public class AppUIConnector extends UIConnector {

    public AppUIConnector() {
        VNotification.setRelativeZIndex(true);
        registerRpc(AppUIClientRpc.class, new AppUIClientRpc() {
            @Override
            public void discardAccumulatedEvents() {
                // silent time
                ValidationErrorHolder.onValidationError();

                ApplicationConnection.MethodInvocationFilter filter =
                        new ApplicationConnection.MethodInvocationFilter() {
                    @Override
                    public boolean apply(MethodInvocation mi) {
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
                    }
                };

                ApplicationConnection.RemoveMethodInvocationCallback callback =
                        new ApplicationConnection.RemoveMethodInvocationCallback() {
                    @Override
                    public void removed(MethodInvocation mi) {
                        ConnectorMap connectorMap = getConnection().getConnectorMap();
                        ServerConnector connector = connectorMap.getConnector(mi.getConnectorId());
                        if (connector instanceof CubaButtonConnector) {
                            ((CubaButtonConnector) connector).stopResponsePending();
                        }
                    }
                };

                getConnection().removePendingInvocationsAndBursts(filter, callback);
            }
        });
    }

    @Override
    protected NotificationDelegate getDelegate() {
        return new CubaNotificationDelegate();
    }

    protected class CubaNotificationDelegate implements NotificationDelegate {

        public static final String CUBA_NOTIFICATION_MODALITY_CURTAIN = "cuba-notification-modalitycurtain";

        private Element modalityCurtain;

        @Override
        public void show(Element overlayContainer, Element element, boolean isShowing, String style, int index) {
            if ("error".equals(style) || "warning".equals(style)) {
                showModalityCurtain(overlayContainer, element, isShowing, index);
            }
        }

        @Override
        public void hide() {
            hideModalityCurtain();
        }

        protected com.google.gwt.user.client.Element getModalityCurtain() {
            if (modalityCurtain == null) {
                modalityCurtain = DOM.createDiv();
                modalityCurtain.setClassName(CUBA_NOTIFICATION_MODALITY_CURTAIN);
            }
            return DOM.asOld(modalityCurtain);
        }

        protected void showModalityCurtain(Element overlayContainer, Element element, boolean isShowing, int index) {
            getModalityCurtain().getStyle().setZIndex(index + VNotification.Z_INDEX_BASE);

            if (isShowing) {
                overlayContainer.insertBefore(getModalityCurtain(), element);
            } else {
                overlayContainer.appendChild(getModalityCurtain());
            }
        }

        protected void hideModalityCurtain() {
            if (modalityCurtain != null) {
                modalityCurtain.removeFromParent();
                modalityCurtain = null;
            }
        }
    }
}