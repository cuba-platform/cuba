/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.horizontalactionslayout;

import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout.CubaOrderedActionsLayoutConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(value = CubaHorizontalActionsLayout.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaHorizontalActionsLayoutConnector extends CubaOrderedActionsLayoutConnector {

    @Override
    public CubaHorizontalActionsLayoutWidget getWidget() {
        return (CubaHorizontalActionsLayoutWidget)  super.getWidget();
    }
}
