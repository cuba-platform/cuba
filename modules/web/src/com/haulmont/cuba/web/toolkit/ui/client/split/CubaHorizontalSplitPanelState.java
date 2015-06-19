/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.split;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.splitpanel.HorizontalSplitPanelState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaHorizontalSplitPanelState extends HorizontalSplitPanelState {

    @NoLayout
    public boolean dockable = false;

    @NoLayout
    public SplitPanelDockMode dockMode = SplitPanelDockMode.LEFT;

    @NoLayout
    public String defaultPosition = null;
}