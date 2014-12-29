/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.filter.operationedit;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Operation editor for PropertyCondition. Displays lookup component for selecting an operation.
 *
 * @author krivopustov
 * @version $Id$
 */
public class PropertyOperationEditor extends AbstractOperationEditor {

    protected ComponentsFactory componentsFactory;
    protected Messages messages;

    public PropertyOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected Component createComponent() {
        componentsFactory = AppBeans.get(ComponentsFactory.class);
        messages = AppBeans.get(Messages.NAME);

        LookupField select = componentsFactory.createComponent(LookupField.NAME);
        FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
        filterHelper.setLookupNullSelectionAllowed(select, false);

        Map<String, Object> values = new LinkedHashMap<>();
        for (Op op : Op.availableOps(condition.getJavaClass())) {
            values.put(messages.getMessage(op), op);
        }
        select.setOptionsMap(values);

        select.setValue((condition).getOperator());
        select.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                Op op = (Op) value;
                (PropertyOperationEditor.this.condition).setOperator(op);
            }
        });

        select.setWidth("100px");
        return select;
    }
}