/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.*;

@Deprecated
@SuppressWarnings("serial")
public class ActionsField extends com.vaadin.ui.CustomField {
    private HorizontalLayout composition;
    private AbstractSelect field;

    public ActionsField(AbstractSelect field) {
        composition = new HorizontalLayout();
        composition.setWidth("100%");

        this.field = field;
        field.setWidth("100%");
        composition.addComponent(field);
        composition.setExpandRatio(field, 1);

        setStyleName("actionsfield");
    }

    @Override
    protected Component initContent() {
        return composition;
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }

    public void addButton(Button button) {
        composition.addComponent(button);
    }

    @Override
    public Object getValue() {
        return field.getValue();
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        if (required) {
            field.setRequired(!required);
        }
        field.setNullSelectionAllowed(!required);
    }

    public AbstractSelect getSelect() {
        return field;
    }

    @Override
    public void focus() {
        field.focus();
    }
}