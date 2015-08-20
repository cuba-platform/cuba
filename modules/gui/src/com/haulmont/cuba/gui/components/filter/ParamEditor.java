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
    private boolean removeButtonVisible;
    protected Component field;
    protected String fieldWidth = null;
    protected Label captionLbl;
    protected Component operationEditor;
    protected Component paramEditComponent;
    protected BoxLayout paramEditComponentLayout;
    protected BoxLayout labelAndOperationLayout;
    protected LinkButton removeButton;
    protected ComponentsFactory componentsFactory;
    protected Action removeButtonAction;

    public ParamEditor(final AbstractCondition condition, boolean removeButtonVisible, boolean operationEditable) {
        this.condition = condition;
        this.removeButtonVisible = removeButtonVisible;

        componentsFactory = AppBeans.get(ComponentsFactory.class);
        labelAndOperationLayout = componentsFactory.createComponent(HBoxLayout.class);
        labelAndOperationLayout.setSpacing(true);
        labelAndOperationLayout.setAlignment(Component.Alignment.MIDDLE_RIGHT);

        captionLbl = componentsFactory.createComponent(Label.class);
        captionLbl.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        captionLbl.setValue(condition.getLocCaption());
        labelAndOperationLayout.add(captionLbl);

        operationEditor = condition.createOperationEditor().getComponent();
        operationEditor.setAlignment(Component.Alignment.MIDDLE_RIGHT);
        operationEditor.setEnabled(operationEditable);
        labelAndOperationLayout.add(operationEditor);

        createParamEditLayout();

        condition.addListener(this);
    }

    public void createParamEditLayout() {
        if (paramEditComponentLayout == null){
            paramEditComponentLayout = componentsFactory.createComponent(HBoxLayout.class);
            paramEditComponentLayout.setSpacing(true);
            paramEditComponentLayout.setWidth("100%");
        }

        paramEditComponent = condition.getParam().createEditComponent(Param.ValueProperty.VALUE);
        paramEditComponent.setAlignment(Component.Alignment.MIDDLE_LEFT);
        if (paramEditComponent instanceof Field) {
            ((Field) paramEditComponent).setRequired(condition.getRequired());
        }
        paramEditComponentLayout.add(paramEditComponent);

        removeButton = componentsFactory.createComponent(LinkButton.class);
        removeButton.setIcon("icons/item-remove.png");
        removeButton.setAlignment(Component.Alignment.MIDDLE_LEFT);
        removeButton.setVisible(removeButtonVisible);
        removeButton.setAction(removeButtonAction);
        paramEditComponentLayout.add(removeButton);

        if (paramEditComponentExpandRequired(condition)) {
            paramEditComponentLayout.expand(paramEditComponent);
        } else {
            HBoxLayout spring = componentsFactory.createComponent(HBoxLayout.class);
            paramEditComponentLayout.add(spring);
            paramEditComponentLayout.expand(spring);
        }
    }

    @Override
    public void paramChanged(Param oldParam, Param newParam) {
        Component oldParamEditComponent = paramEditComponent;
        for (Component component : paramEditComponentLayout.getComponents()) {
            paramEditComponentLayout.remove(component);
        }
        createParamEditLayout();
        if (paramEditComponent instanceof Field) {
            ((Field) paramEditComponent).setRequired(condition.getRequired());
            if (oldParam.getJavaClass().equals(newParam.getJavaClass())
                    && paramEditComponent.getClass().equals(oldParamEditComponent.getClass())) {
                ((Field) paramEditComponent).setValue(((Field) oldParamEditComponent).getValue());
            }
        }
    }

    protected boolean paramEditComponentExpandRequired(AbstractCondition condition) {
        Class paramJavaClass = condition.getParam().getJavaClass();
        return !(Date.class.isAssignableFrom(paramJavaClass)
                || Boolean.class.isAssignableFrom(paramJavaClass)) || condition.getInExpr();
    }

    @Override
    public void captionChanged() {
        captionLbl.setValue(condition.getLocCaption());
    }

    public AbstractCondition getCondition() {
        return condition;
    }

    public BoxLayout getParamEditComponentLayout() {
        return paramEditComponentLayout;
    }

    public BoxLayout getLabelAndOperationLayout() {
        return labelAndOperationLayout;
    }

    public LinkButton getRemoveButton() {
        return removeButton;
    }

    public void requestFocus() {
        paramEditComponent.requestFocus();
    }

    public void setRemoveButtonAction(Action removeButtonAction) {
        this.removeButtonAction = removeButtonAction;
        removeButton.setAction(removeButtonAction);
    }
}