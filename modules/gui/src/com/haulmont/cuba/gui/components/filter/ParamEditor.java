/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;

import java.util.Date;

public class ParamEditor implements AbstractCondition.Listener {

    protected AbstractCondition condition;
    protected FilterDataContext filterDataContext;
    protected boolean removeButtonVisible;
    protected String fieldWidth = null;
    protected Label<String> captionLbl;
    protected Component operationEditor;
    protected Component paramEditComponent;
    protected BoxLayout paramEditComponentLayout;
    protected BoxLayout labelAndOperationLayout;
    protected LinkButton removeButton;
    protected Action removeButtonAction;

    protected UiComponents uiComponents = AppBeans.get(UiComponents.class);

    public ParamEditor(AbstractCondition condition,
                       FilterDataContext filterDataContext,
                       boolean removeButtonVisible,
                       boolean operationEditable) {
        this.condition = condition;
        this.filterDataContext = filterDataContext;
        this.removeButtonVisible = removeButtonVisible;

        labelAndOperationLayout = uiComponents.create(HBoxLayout.class);
        labelAndOperationLayout.setSpacing(true);
        labelAndOperationLayout.setAlignment(Alignment.MIDDLE_RIGHT);

        captionLbl = uiComponents.create(Label.NAME);
        captionLbl.setAlignment(Alignment.MIDDLE_RIGHT);
        captionLbl.setValue(condition.getLocCaption());
        labelAndOperationLayout.add(captionLbl);

        operationEditor = condition.createOperationEditor().getComponent();
        operationEditor.setEnabled(operationEditable);
        labelAndOperationLayout.add(operationEditor);

        // if we add a condition without operation
        if (operationEditor instanceof VBoxLayout
                && ((VBoxLayout) operationEditor).getOwnComponents().isEmpty()) {
            labelAndOperationLayout.addStyleName("no-operation");
        }

        createParamEditLayout();

        condition.addListener(this);
    }

    public void createParamEditLayout() {
        if (paramEditComponentLayout == null){
            paramEditComponentLayout = uiComponents.create(HBoxLayout.class);
            paramEditComponentLayout.setSpacing(true);
            paramEditComponentLayout.setWidthFull();
        }

        paramEditComponent = condition.getParam().createEditComponentForFilterValue(filterDataContext);
        paramEditComponent.addStyleName("param-field");
        if (paramEditComponent instanceof Field) {
            ((Field) paramEditComponent).setRequired(condition.getRequired());
        }
        paramEditComponentLayout.add(paramEditComponent);
        removeButton = uiComponents.create(LinkButton.class);
        removeButton.setStyleName("condition-remove-btn");
        removeButton.setIcon("icons/item-remove.png");
        removeButton.setAlignment(Alignment.MIDDLE_LEFT);
        removeButton.setVisible(removeButtonVisible);
        removeButton.setAction(removeButtonAction);
        paramEditComponentLayout.add(removeButton);

        if (paramEditComponentExpandRequired(condition)) {
            paramEditComponentLayout.expand(paramEditComponent);
        } else {
            HBoxLayout spring = uiComponents.create(HBoxLayout.class);
            paramEditComponentLayout.add(spring);
            paramEditComponentLayout.expand(spring);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void paramChanged(Param oldParam, Param newParam) {
        Component oldParamEditComponent = paramEditComponent;
        for (Component component : paramEditComponentLayout.getComponents()) {
            paramEditComponentLayout.remove(component);
        }
        if (filterDataContext != null) {
            filterDataContext.unregisterParam(oldParam);
        }
        createParamEditLayout();
        if (paramEditComponent instanceof Field) {
            ((Field) paramEditComponent).setRequired(condition.getRequired());
            if (oldParam.getJavaClass().equals(newParam.getJavaClass())
                    && paramEditComponent.getClass().equals(oldParamEditComponent.getClass())) {
                ((Field) paramEditComponent).setValue(((Field) oldParamEditComponent).getValue());
            }
        }
        if (filterDataContext != null) {
            filterDataContext.loadForParam(newParam);
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
        if (paramEditComponent instanceof Component.Focusable) {
            ((Component.Focusable) paramEditComponent).focus();
        }
    }

    public void setRemoveButtonAction(Action removeButtonAction) {
        this.removeButtonAction = removeButtonAction;
        removeButton.setAction(removeButtonAction);
    }
}