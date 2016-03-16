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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.CustomField;

/**
 * Simple wrapper for Cuba components which does not contain Vaadin Field
 *
 */
public class CubaFieldWrapper extends CustomField {

    protected com.haulmont.cuba.gui.components.Component component;

    public CubaFieldWrapper(Component component) {
        this.component = component;
        this.setCaption(" "); // use space in caption for proper layout
    }

    @Override
    protected com.vaadin.ui.Component initContent() {
        return WebComponentsHelper.getComposition(component);
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (component instanceof Component.Editable) {
            ((Component.Editable) component).setEditable(!readOnly);
        } else {
            super.setReadOnly(readOnly);
        }
    }

    @Override
    public boolean isReadOnly() {
        if (component instanceof Component.Editable) {
            return !((Component.Editable) component).isEditable();
        }
        return super.isReadOnly();
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (component != null) {
            if (width < 0) {
                component.setWidth(Component.AUTO_SIZE);
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
                component.setHeight(Component.AUTO_SIZE);
            } else {
                component.setHeight("100%");
            }
        }
    }
}