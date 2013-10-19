/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.Op;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Select;

public class PropertyOperationEditor extends OperationEditor {

    public PropertyOperationEditor(AbstractCondition condition) {
        super(condition);

        final AbstractSelect select = new Select();
        select.setImmediate(true);
        select.setNullSelectionAllowed(false);

        for (Op op : Op.availableOps(condition.getJavaClass())) {
            select.addItem(op);
            select.setItemCaption(op, MessageProvider.getMessage(op));
        }
        select.select(((PropertyCondition) condition).getOperator());

        select.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Op op = (Op) select.getValue();
                ((PropertyCondition) PropertyOperationEditor.this.condition).setOperator(op);
            }
        });

        select.setSizeFull();
        layout.addComponent(select);
    }


}
