/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 12.08.2010 18:32:24
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VFlowLayout;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@ClientWidget(VFlowLayout.class)
public class FlowLayout extends VerticalLayout {
    public FlowLayout() {
        super();
        setHeight("100%");
    }
}
