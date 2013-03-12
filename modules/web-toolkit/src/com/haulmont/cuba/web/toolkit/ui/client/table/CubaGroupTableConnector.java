/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.table;

import com.google.gwt.core.client.GWT;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupTable;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.table.TableConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaGroupTable.class)
public class CubaGroupTableConnector extends TableConnector {

    @Override
    public CubaGroupTableWidget getWidget() {
        return (CubaGroupTableWidget) super.getWidget();
    }

    @Override
    protected CubaGroupTableWidget createWidget() {
        return GWT.create(CubaGroupTableWidget.class);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (uidl.hasVariable("groupColumns"))
            getWidget().updateGroupColumns(uidl.getStringArrayVariableAsSet("groupColumns"));
        else
            getWidget().updateGroupColumns(null);

        super.updateFromUIDL(uidl, client);
    }
}