/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.filter.operationedit;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * Custom condition operation editor. Does nothing.
 * @author krivopustov
 * @version $Id$
 */
public class CustomOperationEditor extends AbstractOperationEditor {

    public CustomOperationEditor(final AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected Component createComponent() {
        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        BoxLayout layout = componentsFactory.createComponent(VBoxLayout.class);
        return layout;
    }
}