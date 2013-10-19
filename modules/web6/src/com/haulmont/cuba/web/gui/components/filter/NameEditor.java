/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class NameEditor extends CustomComponent implements AbstractCondition.Listener {

    protected AbstractCondition condition;
    protected HorizontalLayout layout;
    private Label lab;

    public NameEditor(final AbstractCondition condition) {
        layout = new HorizontalLayout();
        layout.setSizeFull();
        setCompositionRoot(layout);

        lab = new Label(condition.getLocCaption());
        layout.addComponent(lab);

        this.condition = condition;

        condition.addListener(this);

        // TODO fix this - now if we don't set the width explicitly, the name is not visible in the tree table at all
        setWidth(200, UNITS_PIXELS);
    }

    public void captionChanged() {
        layout.removeComponent(lab);
        lab = new Label(condition.getLocCaption());
        layout.addComponent(lab);
    }

    public void paramChanged() {
    }
}
