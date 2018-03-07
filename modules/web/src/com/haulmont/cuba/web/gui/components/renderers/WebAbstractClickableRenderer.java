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

import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.web.gui.components.WebDataGrid;
import com.haulmont.cuba.web.gui.components.WebWrapperUtils;
import com.vaadin.v7.ui.renderers.ClickableRenderer;

public abstract class WebAbstractClickableRenderer<T> extends WebDataGrid.AbstractRenderer<T>
        implements DataGrid.HasRendererClickListener {

    protected DataGrid.RendererClickListener listener;

    public WebAbstractClickableRenderer() {
        this(null);
    }

    public WebAbstractClickableRenderer(DataGrid.RendererClickListener listener) {
        super(null);
        this.listener = listener;
    }

    protected ClickableRenderer.RendererClickListener createClickListenerWrapper(DataGrid.RendererClickListener listener) {
        return (ClickableRenderer.RendererClickListener) e -> {
            DataGrid.Column column = getColumnByGridColumn(e.getColumn());
            DataGrid.RendererClickEvent event = new DataGrid.RendererClickEvent(
                    getDataGrid(), WebWrapperUtils.toMouseEventDetails(e), e.getItemId(), column.getId());
            listener.onClick(event);
        };
    }

    @Override
    public void setRendererClickListener(DataGrid.RendererClickListener listener) {
        checkRendererNotSet();
        this.listener = listener;
    }
}
