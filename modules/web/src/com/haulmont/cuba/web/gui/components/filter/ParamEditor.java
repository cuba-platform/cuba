/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.ui.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ParamEditor extends HorizontalLayout implements AbstractCondition.Listener {

    protected AbstractCondition<Param> condition;
    protected Component field;
    protected String fieldWidth = null;
    protected boolean applyRequired;

    public ParamEditor(final AbstractCondition<Param> condition,
                       boolean showOperation, boolean showCaption, boolean applyRequired) {

        this.condition = condition;

        setSpacing(true);
        setSizeUndefined();
        setStyleName("cuba-generic-filter-parameditor");

        if (condition.getParam() != null) {
            if (showCaption) {
                Label parameterNameLabel = new Label(condition.getLocCaption());
                addComponent(parameterNameLabel);
                setComponentAlignment(parameterNameLabel, Alignment.MIDDLE_LEFT);
            }
            if (showOperation) {
                Label opLab = new Label(condition.getOperationCaption());
                addComponent(opLab);
                setComponentAlignment(opLab, Alignment.MIDDLE_LEFT);
            }
            field = condition.getParam().createEditComponent();
            if (field != null && AppUI.getCurrent().isTestMode()) {
                field.setCubaId("field");
            }

            this.applyRequired = applyRequired;
            if (applyRequired && field instanceof Field) {
                ((Field) field).setRequired(condition.isRequired());
            }
            addComponent(field);
        }

        condition.addListener(this);
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && field != null) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            field.setId(testIdManager.getTestId(id + "_field"));
        }
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
        if (applyRequired && field instanceof Field) {
            ((Field) field).setRequired(condition.isRequired());
        }
        field.setWidth(fieldWidth);

        // Test mode
        if (getId() != null) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            field.setId(testIdManager.getTestId(getId() + "_field"));
            field.setCubaId("field");
        }
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