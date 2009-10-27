/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.10.2009 12:58:31
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class OperationEditor extends CustomComponent {

    protected Condition condition;
    protected VerticalLayout layout;

    public OperationEditor(Condition condition) {
        layout = new VerticalLayout();
        layout.setSizeFull();
        setCompositionRoot(layout);

        this.condition = condition;
    }
}
