/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.split;

import com.vaadin.shared.ui.splitpanel.HorizontalSplitPanelState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaHorizontalSplitPanelState extends HorizontalSplitPanelState {

    public boolean dockable = false;

    public SplitPanelDockMode dockMode = SplitPanelDockMode.LEFT;

    public String defaultPosition = null;
}