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

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.web.gui.components.WebDataGrid;
import com.haulmont.cuba.web.widgets.renderers.componentrenderer.ComponentRenderer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.renderers.Renderer;

import java.util.Locale;

/**
 * A renderer for UI components.
 */
public class WebComponentRenderer extends WebDataGrid.AbstractRenderer<com.vaadin.ui.Component>
        implements DataGrid.ComponentRenderer {

    @Override
    protected Renderer<com.vaadin.ui.Component> createImplementation() {
        return new ComponentRenderer();
    }

    @Override
    public Converter getConverter() {
        return new Converter<com.vaadin.ui.Component, Component>() {
            @Override
            public Component convertToModel(com.vaadin.ui.Component value,
                                            Class<? extends Component> targetType, Locale locale) {
                // do nothing
                return null;
            }

            @Override
            public com.vaadin.ui.Component convertToPresentation(Component value,
                                                                 Class<? extends com.vaadin.ui.Component> targetType,
                                                                 Locale locale) {
                return value.unwrap(com.vaadin.ui.Component.class);
            }

            @Override
            public Class<Component> getModelType() {
                return Component.class;
            }

            @Override
            public Class<com.vaadin.ui.Component> getPresentationType() {
                return com.vaadin.ui.Component.class;
            }
        };
    }
}
