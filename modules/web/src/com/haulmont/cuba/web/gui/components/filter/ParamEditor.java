/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.vaadin.ui.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ParamEditor extends HorizontalLayout implements AbstractCondition.Listener {

    private AbstractCondition<Param> condition;
    private Component field;

    public ParamEditor(final AbstractCondition<Param> condition, boolean showOperation) {
        this.condition = condition;

        setSpacing(true);
        setHeight("-1px");

        if (condition.getParam() != null) {
            if (showOperation) {
                Label opLab = new Label(condition.getOperationCaption());
                addComponent(opLab);
            }
            field = condition.getParam().createEditComponent();
            if (field instanceof Field) {
                ((Field) field).setRequired(condition.isRequired());
            }
            addComponent(field);
        }

        condition.addListener(this);
    }

    @Override
    public void paramChanged() {
        if (field != null) {
            removeComponent(field);
        }
        field = condition.getParam().createEditComponent();
        addComponent(field);
    }

    @Override
    public void captionChanged() {
    }

    public void setFocused() {
        ((Focusable) field).focus();
    }

    public AbstractCondition<Param> getCondition() {
        return condition;
    }
}