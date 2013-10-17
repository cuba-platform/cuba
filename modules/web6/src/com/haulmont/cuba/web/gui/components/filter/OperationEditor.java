/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.10.2009 12:58:31
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractOperationEditor;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class OperationEditor extends AbstractOperationEditor<CustomComponent> {

    protected Layout layout;

    public OperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected void createEditor() {
      impl =  new Editor();
    }

    protected class Editor extends CustomComponent {
        public Editor() {
            layout = new VerticalLayout();
            layout.setSizeFull();
            setCompositionRoot(layout);
        }
    }
}
