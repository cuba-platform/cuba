/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.10.2009 14:43:55
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.data.Property;
import com.haulmont.cuba.core.global.MessageProvider;
import org.apache.commons.lang.ObjectUtils;

public class PropertyOperationEditor extends OperationEditor {

    public PropertyOperationEditor(Condition condition) {
        super(condition);

        final AbstractSelect select = new NativeSelect();
        select.setImmediate(true);
        select.setNullSelectionAllowed(false);

        for (PropertyCondition.Op op : PropertyCondition.Op.availableOps(condition.getJavaClass())) {
            select.addItem(op);
            select.setItemCaption(op, MessageProvider.getMessage(op));
        }
        select.select(((PropertyCondition) condition).getOperator());

        select.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                PropertyCondition.Op op = (PropertyCondition.Op) select.getValue();
                ((PropertyCondition) PropertyOperationEditor.this.condition).setOperator(op);
            }
        });

        select.setSizeFull();
        layout.addComponent(select);
    }


}
