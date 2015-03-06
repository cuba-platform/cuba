/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.appui;

import com.haulmont.cuba.web.AppUI;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ui.ui.UIConnector;
import com.vaadin.shared.communication.LegacyChangeVariablesInvocation;
import com.vaadin.shared.communication.MethodInvocation;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.button.ButtonServerRpc;
import com.vaadin.shared.ui.tabsheet.TabsheetServerRpc;

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
                // silent time
                ValidationErrorHolder.onValidationError();

                getConnection().removePendingInvocationsAndBursts(new ApplicationConnection.MethodInvocationFilter() {
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
                });
            }
        });
    }
}