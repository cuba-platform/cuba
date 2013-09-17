/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ParamEditor extends HorizontalLayout implements AbstractCondition.Listener {

    protected AbstractCondition<Param> condition;
    protected Component field;
    protected String fieldWidth = null;

    public ParamEditor(final AbstractCondition<Param> condition, boolean showOperation, boolean showCaption) {
        this.condition = condition;

        setSpacing(true);
        setSizeUndefined();
        setStyleName("cuba-generic-filter-parameditor");

        if (condition.getParam() != null) {
            if (showCaption) {
                Label parameterNameLabel = new Label(condition.getLocCaption());
                addComponent(parameterNameLabel);
            }
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

    public void setFieldWidth(String fieldWidth) {
        this.fieldWidth = fieldWidth;
        if (field != null) {
            field.setWidth(fieldWidth);
        }
    }

    @Override
    public void paramChanged() {
        if (field != null) {
            removeComponent(field);
        }
        field = condition.getParam().createEditComponent();
        field.setWidth(fieldWidth);
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