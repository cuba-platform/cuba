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
import com.haulmont.cuba.web.widgets.renderers.CubaClickableTextRenderer;
import com.vaadin.ui.renderers.Renderer;

/**
 * A renderer for presenting simple plain-text string values as a link with call back handler.
 */
public class WebClickableTextRenderer<T extends Entity>
        extends WebAbstractClickableRenderer<T, String>
        implements DataGrid.ClickableTextRenderer<T> {

    public WebClickableTextRenderer() {
        this("");
    }

    public WebClickableTextRenderer(String nullRepresentation) {
        this(null, nullRepresentation);
    }

    public WebClickableTextRenderer(DataGrid.RendererClickListener<T> listener) {
        this(listener, "");
    }

    public WebClickableTextRenderer(DataGrid.RendererClickListener<T> listener, String nullRepresentation) {
        super(listener);
        this.nullRepresentation = nullRepresentation;
    }

    @Override
    protected Renderer<String> createImplementation() {
        if (listener != null) {
            return new CubaClickableTextRenderer<>(createClickListenerWrapper(listener), getNullRepresentation());
        } else {
            return new CubaClickableTextRenderer<>(getNullRepresentation());
        }
    }

    @Override
    public String getNullRepresentation() {
        return super.getNullRepresentation();
    }

    @Override
    public void setNullRepresentation(String nullRepresentation) {
        super.setNullRepresentation(nullRepresentation);
    }
}
