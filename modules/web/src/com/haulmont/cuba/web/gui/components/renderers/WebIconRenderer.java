/*
 * Copyright (c) 2008-2019 Haulmont.
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
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.web.gui.components.WebAbstractDataGrid.AbstractRenderer;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.renderers.CubaIconRenderer;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Resource;
import com.vaadin.ui.renderers.Renderer;

import javax.inject.Inject;

public class WebIconRenderer<T extends Entity>
        extends AbstractRenderer<T, Resource> implements DataGrid.IconRenderer<T> {

    protected Icons icons;
    protected IconResolver iconResolver;

    public WebIconRenderer() {
    }

    @Inject
    public void setIcons(Icons icons) {
        this.icons = icons;
    }

    @Inject
    public void setIconResolver(IconResolver iconResolver) {
        this.iconResolver = iconResolver;
    }

    @Override
    protected Renderer<Resource> createImplementation() {
        return new CubaIconRenderer();
    }

    @Override
    public ValueProvider<Icons.Icon, Resource> getPresentationValueProvider() {
        return (ValueProvider<Icons.Icon, Resource>) icon -> {
            String iconName = icons.get(icon);
            return iconResolver.getIconResource(iconName);
        };
    }
}
