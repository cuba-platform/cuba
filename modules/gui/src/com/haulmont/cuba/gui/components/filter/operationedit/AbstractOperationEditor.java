/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.operationedit;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;

/**
 * @author devyatkin
 * @version $Id$
 */
public abstract class AbstractOperationEditor {

    protected Component component;
    protected AbstractCondition condition;

    public AbstractOperationEditor(AbstractCondition condition) {
        this.condition = condition;
        this.component = createComponent();
    }

    protected abstract Component createComponent();

    public Component getComponent() {
        return component;
    }
}