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
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.DataGrid.RendererClickListener;
import com.haulmont.cuba.web.gui.components.WebAbstractDataGrid.AbstractRenderer;
import com.haulmont.cuba.web.gui.components.WebWrapperUtils;
import com.vaadin.ui.renderers.ClickableRenderer;

public abstract class WebAbstractClickableRenderer<T extends Entity, V> extends AbstractRenderer<T, V>
        implements DataGrid.HasRendererClickListener<T> {

    protected RendererClickListener<T> listener;

    public WebAbstractClickableRenderer() {
        this(null);
    }

    public WebAbstractClickableRenderer(RendererClickListener<T> listener) {
        super(null);
        this.listener = listener;
    }

    protected ClickableRenderer.RendererClickListener<T> createClickListenerWrapper(RendererClickListener<T> listener) {
        return (ClickableRenderer.RendererClickListener<T>) e -> {
            DataGrid.Column column = getColumnByGridColumn(e.getColumn());
            DataGrid.RendererClickEvent<T> event = new DataGrid.RendererClickEvent<>(getDataGrid(),
                    WebWrapperUtils.toMouseEventDetails(e), e.getItem(), column.getId());
            listener.onClick(event);
        };
    }

    @Override
    public void setRendererClickListener(RendererClickListener<T> listener) {
        checkRendererNotSet();
        this.listener = listener;
    }
}
