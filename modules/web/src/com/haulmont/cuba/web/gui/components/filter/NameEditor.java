/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.10.2009 12:17:07
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class NameEditor extends CustomComponent implements Condition.Listener {

    protected Condition condition;
    protected HorizontalLayout layout;
    private Label lab;

    public NameEditor(final Condition condition) {
        layout = new HorizontalLayout();
        layout.setSizeFull();
        setCompositionRoot(layout);

        lab = new Label(condition.getLocCaption());
        layout.addComponent(lab);

        this.condition = condition;

        condition.addListener(this);
    }

    public void captionChanged() {
        layout.removeComponent(lab);
        lab = new Label(condition.getLocCaption());
        layout.addComponent(lab);
    }

    public void paramChanged() {
    }
}
