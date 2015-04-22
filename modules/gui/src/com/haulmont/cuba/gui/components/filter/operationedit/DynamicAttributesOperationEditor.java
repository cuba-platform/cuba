/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.operationedit;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * Operation editor for RuntimeProperties conditions. Just displays condition label, doesn't allow to edit condition.
 *
 * @author devyatkin
 * @version $Id$
 */
public class DynamicAttributesOperationEditor extends PropertyOperationEditor {

    public DynamicAttributesOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected Component createComponent() {
        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        Label label = componentsFactory.createComponent(Label.NAME);
        label.setValue(condition.getOperationCaption());
        return label;
    }

}