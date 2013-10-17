/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Gennady Pavlov
 * Created: 08.04.2010 11:21:57
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;

@SuppressWarnings("serial")
public class ActionsField extends CustomField {
    private GridLayout root;
    private AbstractSelect field;

    public ActionsField(AbstractSelect field) {
        root = new GridLayout();
        root.setWidth("100%");
        setCompositionRoot(root);

        this.field = field;
        field.setWidth("100%");
        root.addComponent(field);
        root.setColumnExpandRatio(0, 1);

        setStyleName("actionsfield");
    }

    public Class<?> getType() {
        return String.class;
    }

    public void addButton(Button button) {
        root.setCursorX(root.getColumns());
        root.setCursorY(0);
        root.addComponent(button);
        root.setColumnExpandRatio(root.getColumns() - 1, 0);
        root.setComponentAlignment(button, Alignment.TOP_RIGHT);
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
