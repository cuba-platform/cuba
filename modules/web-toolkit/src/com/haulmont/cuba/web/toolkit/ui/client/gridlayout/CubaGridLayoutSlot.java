/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.gridlayout;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.ManagedLayout;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaGridLayoutSlot extends ComponentConnectorLayoutSlot {

    public CubaGridLayoutSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);
    }
}