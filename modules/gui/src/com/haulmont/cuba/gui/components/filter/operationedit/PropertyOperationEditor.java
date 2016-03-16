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
package com.haulmont.cuba.gui.components.filter.operationedit;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.gui.components.filter.OpManager;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * Operation editor for PropertyCondition. Displays popupButton component for selecting an operation.
 *
 */
public class PropertyOperationEditor extends AbstractOperationEditor {

    protected ComponentsFactory componentsFactory;
    protected Messages messages;
    protected PopupButton popupButton;

    public PropertyOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected Component createComponent() {
        componentsFactory = AppBeans.get(ComponentsFactory.class);
        messages = AppBeans.get(Messages.NAME);
        popupButton = componentsFactory.createComponent(PopupButton.class);

        OpManager opManager = AppBeans.get(OpManager.class);
        for (Op op : opManager.availableOps(condition.getJavaClass())) {
            OperatorChangeAction operatorChangeAction = new OperatorChangeAction(op);
            popupButton.addAction(operatorChangeAction);
        }

        popupButton.setCaption(condition.getOperator().getLocCaption());
        popupButton.setStyleName("condition-operation-button");

        return popupButton;
    }

    protected class OperatorChangeAction extends AbstractAction {
        protected Op op;

        public OperatorChangeAction(Op op) {
            super(op.toString());
            this.op = op;
        }

        @Override
        public void actionPerform(Component component) {
            (PropertyOperationEditor.this.condition).setOperator(op);
            popupButton.setCaption(getCaption());
        }

        @Override
        public String getCaption() {
            return op.getLocCaption();
        }
    }
}