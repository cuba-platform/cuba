/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.verticalactionslayout;

import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout.CubaOrderedActionsLayoutConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(value = CubaVerticalActionsLayout.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaVerticalActionsLayoutConnector extends CubaOrderedActionsLayoutConnector {

    @Override
    public CubaVerticalActionsLayoutWidget getWidget() {
        return (CubaVerticalActionsLayoutWidget) super.getWidget();
    }
}