/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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