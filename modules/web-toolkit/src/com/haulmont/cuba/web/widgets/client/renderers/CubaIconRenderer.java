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

package com.haulmont.cuba.web.widgets.client.renderers;

import com.google.gwt.dom.client.Element;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.ImageIcon;
import com.vaadin.client.widget.grid.RendererCellReference;

public class CubaIconRenderer implements Renderer<Icon> {

    @Override
    public void render(RendererCellReference cell, Icon icon) {
        if (icon instanceof ImageIcon) {
            // onload will set appropriate size later
            icon.setWidth("0");
            icon.setHeight("0");
        }

        Element iconElement = icon.getElement();
        cell.getElement().setInnerHTML(getOuterHtml(iconElement));

    }

    protected native String getOuterHtml(Element element) /*-{
        return element.outerHTML;
    }-*/;
}
