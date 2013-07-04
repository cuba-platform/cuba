/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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