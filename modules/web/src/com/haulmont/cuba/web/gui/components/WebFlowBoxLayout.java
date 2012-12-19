/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 17.08.2010 14:29:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.FlowBoxLayout;
import com.haulmont.cuba.toolkit.gwt.client.ui.VFlowLayout;
import com.vaadin.ui.ClientWidget;

@SuppressWarnings("serial")
@ClientWidget(VFlowLayout.class)
public class WebFlowBoxLayout extends WebVBoxLayout implements FlowBoxLayout {

    public WebFlowBoxLayout() {
        setHeight("100%");
    }
}
