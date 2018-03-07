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
import com.haulmont.cuba.web.gui.components.WebDataGrid.AbstractRenderer;
import com.vaadin.v7.ui.renderers.TextRenderer;

/**
 * A renderer for presenting simple plain-text string values.
 */
public class WebTextRenderer extends AbstractRenderer<String> implements DataGrid.TextRenderer {

    public WebTextRenderer() {
        this("");
    }

    public WebTextRenderer(String nullRepresentation) {
        super(nullRepresentation);
    }

    @Override
    public TextRenderer getImplementation() {
        return (TextRenderer) super.getImplementation();
    }

    @Override
    protected TextRenderer createImplementation() {
        return new TextRenderer(getNullRepresentation());
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