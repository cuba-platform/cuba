/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractOperationEditor;
import com.vaadin.ui.VerticalLayout;

/**
 * @author krivopustov
 * @version $Id$
 */
public class OperationEditor extends AbstractOperationEditor<VerticalLayout> {

    public OperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected void createEditor() {
        impl = new Editor();
    }

    protected class Editor extends VerticalLayout {
        public Editor() {
            setSizeFull();
        }
    }
}