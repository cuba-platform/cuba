/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
            Messages messages = AppBeans.get(Messages.NAME);
            select.setItemCaption(op, messages.getMessage(op));
        }
        select.select(((PropertyCondition) condition).getOperator());

        select.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Op op = (Op) select.getValue();
                ((PropertyCondition) PropertyOperationEditor.this.condition).setOperator(op);
            }
        });

        select.setWidth("100%");
        impl.addComponent(select);
    }
}