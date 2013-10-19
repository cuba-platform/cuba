/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.FlowBoxLayout;
import com.haulmont.cuba.toolkit.gwt.client.ui.VFlowLayout;
import com.haulmont.cuba.web.toolkit.ui.FlowLayout;
import com.vaadin.ui.ClientWidget;

/**
 * @author gorodnov
 * @version $Id$
 */
@ClientWidget(VFlowLayout.class)
public class WebFlowBoxLayout extends WebVBoxLayout implements FlowBoxLayout {

    public WebFlowBoxLayout() {
        component = new FlowLayout();
    }
}
