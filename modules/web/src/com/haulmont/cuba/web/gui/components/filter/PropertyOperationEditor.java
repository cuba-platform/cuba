/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.Op;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PropertyOperationEditor extends OperationEditor {

    public PropertyOperationEditor(AbstractCondition condition) {
        super(condition);

        final AbstractSelect select = new ComboBox();
        select.setImmediate(true);
        select.setNullSelectionAllowed(false);

        for (Op op : Op.availableOps(condition.getJavaClass())) {
            select.addItem(op);
            select.setItemCaption(op, AppBeans.get(Messages.class).getMessage(op));
        }
        select.select(((PropertyCondition) condition).getOperator());

        select.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Op op = (Op) select.getValue();
                ((PropertyCondition) PropertyOperationEditor.this.condition).setOperator(op);
            }
        });

        select.setSizeFull();
        layout.addComponent(select);
    }
}