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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * Operation editor for RuntimeProperties conditions. Just displays condition label, doesn't allow to edit condition.
 *
 */
public class DynamicAttributesOperationEditor extends PropertyOperationEditor {

    public DynamicAttributesOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected Component createComponent() {
        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        Label label = componentsFactory.createComponent(Label.class);
        label.setValue(condition.getOperationCaption());
        return label;
    }

}