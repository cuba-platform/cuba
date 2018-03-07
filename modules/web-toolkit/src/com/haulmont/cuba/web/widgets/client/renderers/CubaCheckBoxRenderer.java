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

package com.haulmont.cuba.web.widgets.client.renderers;

import com.vaadin.v7.client.renderers.Renderer;
import com.vaadin.v7.client.widget.grid.RendererCellReference;

public class CubaCheckBoxRenderer implements Renderer<Boolean> {

    protected static final String BASE_STYLE = "boolean-value";

    @Override
    public void render(RendererCellReference cell, Boolean data) {
        cell.getElement().setInnerHTML(getHtmlString(data));
    }

    protected String getHtmlString(Boolean value) {
        return "<div class=\"" + BASE_STYLE + " " + BASE_STYLE + "-" + value + "\"/>";
    }
}