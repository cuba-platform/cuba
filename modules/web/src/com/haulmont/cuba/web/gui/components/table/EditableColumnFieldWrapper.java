/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components.table;

import com.haulmont.cuba.gui.components.Field;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;

public class EditableColumnFieldWrapper extends CustomField {

    protected Component component;

    public EditableColumnFieldWrapper(Component component, com.haulmont.cuba.gui.components.Component columnComponent) {
        this.component = component;

        if (component.getWidth() < 0) {
            setWidthUndefined();
        }

        if (columnComponent instanceof Field) {
            AbstractComponent vComponent = columnComponent.unwrap(AbstractComponent.class);
            if (vComponent instanceof Focusable) {
                setFocusDelegate((Focusable) vComponent);
            }
        }
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        return component;
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (component != null) {
            if (width < 0) {
                component.setWidth(com.haulmont.cuba.gui.components.Component.AUTO_SIZE);
            } else {
                component.setWidth("100%");
            }
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (component != null) {
            if (height < 0) {
                component.setHeight(com.haulmont.cuba.gui.components.Component.AUTO_SIZE);
            } else {
                component.setHeight("100%");
            }
        }
    }
}