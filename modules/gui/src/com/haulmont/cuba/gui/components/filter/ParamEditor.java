/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ParamEditor implements AbstractCondition.Listener {

    protected AbstractCondition condition;
    protected Component field;
    protected String fieldWidth = null;
    protected BoxLayout mainLayout;
    protected BoxLayout component;
    protected Label captionLbl;
    protected Component operationEditor;
    protected Component paramEditComponent;
    protected final LinkButton removeButton;

    public ParamEditor(final AbstractCondition condition, boolean removeButtonVisible) {
        this.condition = condition;

        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        component = componentsFactory.createComponent(BoxLayout.HBOX);
        component.setWidth("100%");
        mainLayout = componentsFactory.createComponent(BoxLayout.HBOX);
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        component.add(mainLayout);

        captionLbl = componentsFactory.createComponent(Label.NAME);
        captionLbl.setAlignment(Component.Alignment.MIDDLE_LEFT);
        captionLbl.setValue(condition.getLocCaption());
        mainLayout.add(captionLbl);

        operationEditor = condition.createOperationEditor().getComponent();
        operationEditor.setAlignment(Component.Alignment.MIDDLE_LEFT);
        mainLayout.add(operationEditor);

        paramEditComponent = condition.getParam().createEditComponent(Param.ValueProperty.VALUE);
        paramEditComponent.setAlignment(Component.Alignment.MIDDLE_LEFT);
        if (paramEditComponent instanceof Field)
            ((Field) paramEditComponent).setRequired(condition.getRequired());
        mainLayout.add(paramEditComponent);
        mainLayout.expand(paramEditComponent);

        removeButton = componentsFactory.createComponent(LinkButton.NAME);
        removeButton.setIcon("icons/item-remove.png");
        removeButton.setAlignment(Component.Alignment.MIDDLE_LEFT);
        component.add(removeButton);
        removeButton.setVisible(removeButtonVisible);
        component.expand(mainLayout);

        condition.addListener(this);
    }

    @Override
    public void paramChanged() {
        mainLayout.remove(paramEditComponent);
        paramEditComponent = condition.getParam().createEditComponent(Param.ValueProperty.VALUE);
        if (paramEditComponent instanceof Field)
            ((Field) paramEditComponent).setRequired(condition.getRequired());
        mainLayout.add(paramEditComponent);
        mainLayout.expand(paramEditComponent);
    }

    @Override
    public void captionChanged() {
        captionLbl.setValue(condition.getLocCaption());
    }

    public AbstractCondition getCondition() {
        return condition;
    }

    public Component getComponent() {
        return component;
    }

    public LinkButton getRemoveButton() {
        return removeButton;
    }

    public void requestFocus() {
        paramEditComponent.requestFocus();
    }
}