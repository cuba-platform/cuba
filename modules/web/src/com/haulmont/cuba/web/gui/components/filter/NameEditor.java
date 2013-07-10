/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * @author krivopustov
 * @version $Id$
 */
public class NameEditor extends HorizontalLayout implements AbstractCondition.Listener {

    protected AbstractCondition condition;
    private Label lab;

    public NameEditor(final AbstractCondition condition) {
        setSizeFull();

        lab = new Label(condition.getLocCaption());
        addComponent(lab);

        this.condition = condition;

        condition.addListener(this);
    }

    @Override
    public void captionChanged() {
        removeComponent(lab);
        lab = new Label(condition.getLocCaption());
        addComponent(lab);
    }

    @Override
    public void paramChanged() {
    }
}