/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.vaadin.shared.ui.panel.PanelState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaGroupBoxState extends PanelState {
    {
        primaryStyleName = "cuba-groupbox";
    }

    public boolean collapsable = false;

    public boolean expanded = true;
}