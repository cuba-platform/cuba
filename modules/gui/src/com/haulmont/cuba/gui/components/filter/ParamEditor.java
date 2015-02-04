/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ParamEditor implements AbstractCondition.Listener {

    protected AbstractCondition condition;
    protected Component field;
    protected String fieldWidth = null;
    protected BoxLayout mainLayout;
    protected Label captionLbl;
    protected Component operationEditor;
    protected Component paramEditComponent;
    protected final LinkButton removeButton;

    public ParamEditor(final AbstractCondition condition, boolean removeButtonVisible) {
        this.condition = condition;

        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        mainLayout = componentsFactory.createComponent(BoxLayout.HBOX);
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);

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
        if (Date.class.isAssignableFrom(condition.getParam().getJavaClass()) || Boolean.class.isAssignableFrom(condition.getParam().getJavaClass())) {
            HBoxLayout componentEditLayout = componentsFactory.createComponent(HBoxLayout.class);
            componentEditLayout.add(paramEditComponent);
            paramEditComponent.setAlignment(Component.Alignment.MIDDLE_LEFT);
            mainLayout.add(componentEditLayout);
            mainLayout.expand(componentEditLayout);
        } else {
            mainLayout.add(paramEditComponent);
            mainLayout.expand(paramEditComponent);
        }

        removeButton = componentsFactory.createComponent(LinkButton.NAME);
        removeButton.setIcon("icons/item-remove.png");
        removeButton.setAlignment(Component.Alignment.MIDDLE_LEFT);
        removeButton.setVisible(removeButtonVisible);
        mainLayout.add(removeButton);

        condition.addListener(this);
    }

    @Override
    public void paramChanged(Param oldParam, Param newParam) {
        Component oldParamEditComponent = paramEditComponent;
        mainLayout.remove(paramEditComponent);
        paramEditComponent = condition.getParam().createEditComponent(Param.ValueProperty.VALUE);
        if (paramEditComponent instanceof Field) {
            ((Field) paramEditComponent).setRequired(condition.getRequired());
            if (oldParam.getJavaClass().equals(newParam.getJavaClass())
                    && paramEditComponent.getClass().equals(oldParamEditComponent.getClass())) {
                ((Field) paramEditComponent).setValue(((Field) oldParamEditComponent).getValue());
            }
        }
        mainLayout.add(paramEditComponent, 2);
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
        return mainLayout;
    }

    public LinkButton getRemoveButton() {
        return removeButton;
    }

    public void requestFocus() {
        paramEditComponent.requestFocus();
    }
}