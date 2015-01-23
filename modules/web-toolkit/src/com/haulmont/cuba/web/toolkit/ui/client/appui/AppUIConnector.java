/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.appui;

import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.toolkit.ui.client.timer.CubaTimerServerRpc;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ui.ui.UIConnector;
import com.vaadin.shared.communication.MethodInvocation;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.ui.UIServerRpc;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(AppUI.class)
public class AppUIConnector extends UIConnector {

    public AppUIConnector() {
        registerRpc(AppUIClientRpc.class, new AppUIClientRpc() {
            @Override
            public void discardAccumulatedEvents() {
                getConnection().removePendingInvocationsAndBursts(new ApplicationConnection.MethodInvocationFilter() {
                    @Override
                    public boolean apply(MethodInvocation mi) {
                        // filter timers
                        if (CubaTimerServerRpc.class.getName().equals(mi.getInterfaceName())) {
                            return false;
                        }
                        // filter polling events
                        //noinspection RedundantIfStatement
                        if (UIServerRpc.class.getName().equals(mi.getInterfaceName())
                                && "poll".equals(mi.getMethodName())) {
                            return false;
                        }

                        return true;
                    }
                });
            }
        });
    }
}