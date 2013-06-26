/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.treetable;

import com.haulmont.cuba.web.toolkit.ui.CubaTreeTable;
import com.vaadin.client.ui.VTreeTable;
import com.vaadin.client.ui.treetable.TreeTableConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaTreeTable.class)
public class CubaTreeTableConnector extends TreeTableConnector {

    @Override
    public CubaTreeTableWidget getWidget() {
        return (CubaTreeTableWidget) super.getWidget();
    }
}