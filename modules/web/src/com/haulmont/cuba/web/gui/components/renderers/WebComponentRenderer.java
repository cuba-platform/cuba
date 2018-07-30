/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.gui.components.renderers;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.web.gui.components.WebAbstractDataGrid.AbstractRenderer;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.Renderer;

/**
 * A renderer for UI components.
 */
public class WebComponentRenderer<T extends Entity> extends AbstractRenderer<T, com.vaadin.ui.Component>
        implements DataGrid.ComponentRenderer {

    @Override
    protected Renderer<com.vaadin.ui.Component> createImplementation() {
        return new ComponentRenderer();
    }

    @Override
    public ValueProvider<Component, com.vaadin.ui.Component> getPresentationValueProvider() {
        return (ValueProvider<Component, com.vaadin.ui.Component>) value ->
                value.unwrap(com.vaadin.ui.Component.class);
    }
}
