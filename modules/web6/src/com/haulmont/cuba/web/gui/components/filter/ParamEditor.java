/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.10.2009 14:36:33
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.vaadin.ui.*;

public class ParamEditor extends CustomComponent implements AbstractCondition.Listener {

    private AbstractCondition<Param> condition;
    private HorizontalLayout layout;
    private Component field;

    public ParamEditor(final AbstractCondition<Param> condition, boolean showOperation) {
        this.condition = condition;

        layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();
        setCompositionRoot(layout);

        if (condition.getParam() != null) {
            if (showOperation) {
                Label opLab = new Label(condition.getOperationCaption());
                layout.addComponent(opLab);
            }
            field = condition.getParam().createEditComponent();
            if (field instanceof Field) {
                ((Field) field).setRequired(condition.isRequired());
            }
            layout.addComponent(field);
        }

        condition.addListener(this);
    }

    @Override
    public void paramChanged() {
        if (field != null) {
            layout.removeComponent(field);
        }
        field = condition.getParam().createEditComponent();
        layout.addComponent(field);
    }

    @Override
    public void captionChanged() {
    }

    public void setFocused() {
        ((Focusable) field).focus();
    }

    public AbstractCondition<Param> getCondition() {
        return condition;
    }
}
