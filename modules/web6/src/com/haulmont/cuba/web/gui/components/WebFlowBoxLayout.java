/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.FlowBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.FlowLayout;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebFlowBoxLayout extends WebVBoxLayout implements FlowBoxLayout {

    public WebFlowBoxLayout() {
        component = new FlowLayout();
    }
}
