/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.Op;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class PropertyOperationEditor extends OperationEditor{
    public PropertyOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected void createEditor() {
        final JComboBox select = new ExtendedComboBox();
        select.setPreferredSize(new Dimension(100, 22));
        select.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        boolean selected=false;
        for (Op op : Op.availableOps(condition.getJavaClass())) {
            ItemWrapper<Op> wrapper = new ItemWrapper<Op>(op, MessageProvider.getMessage(op));
            select.addItem(wrapper);
            if (op.equals(((PropertyCondition) condition).getOperator())) {
                select.setSelectedItem(wrapper);
                selected=true;
            }
        }
        if (!selected){
            select.setSelectedItem(null);
        }

        select.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED != e.getStateChange()) {
                    return;
                }
                Op op = ((ItemWrapper<Op>) e.getItem()).getItem();
                ((PropertyCondition) PropertyOperationEditor.this.condition).setOperator(op);
            }
        });
        impl = select;
    }
}
