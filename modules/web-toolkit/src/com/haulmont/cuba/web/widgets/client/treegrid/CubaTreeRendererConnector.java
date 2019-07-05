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

package com.haulmont.cuba.web.widgets.client.treegrid;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.vaadin.client.connectors.grid.TreeRendererConnector;
import com.vaadin.client.renderers.HtmlRenderer;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.RendererCellReference;
import com.vaadin.shared.ui.Connect;
import com.vaadin.ui.Tree;
import elemental.json.JsonObject;

import javax.annotation.Nullable;
import java.util.Objects;

@Connect(Tree.TreeRenderer.class)
public class CubaTreeRendererConnector extends TreeRendererConnector {

    protected static final String V_CAPTIONTEXT_STYLENAME = "v-captiontext";
    protected static final String ITEM_ICON = "itemIcon";

    @Override
    public Renderer<String> createRenderer() {
        return new HtmlRenderer() {

            @Override
            public void render(RendererCellReference cell, String htmlString) {
                String content = getContentString(htmlString);
                Element icon = getIconElement(cell);
                Element span = findSpan(cell);

                if (span == null) {
                    _render(cell, content, icon);
                } else {
                    String oldContent = span.getInnerHTML();
                    if (!Objects.equals(content, oldContent)) {
                        _render(cell, content, icon);
                    }
                }
            }

            protected Element findSpan(RendererCellReference cell) {
                TableCellElement cellEl = cell.getElement();
                int childCount = DOM.getChildCount(cellEl);
                for (int i = 0; i < childCount; i++) {
                    Element child = DOM.getChild(cellEl, i);
                    if (SpanElement.TAG.equalsIgnoreCase(child.getTagName())) {
                        return child;
                    }
                }
                return null;
            }

            protected Element getIconElement(RendererCellReference cell) {
                Element iconEl = null;

                JsonObject row = getParent().getParent().getDataSource()
                        .getRow(cell.getRowIndex());

                if (row != null && row.hasKey(ITEM_ICON)) {
                    String resourceUrl = getResourceUrl(row.getString(ITEM_ICON));
                    iconEl = getConnection().getIcon(resourceUrl)
                            .getElement();
                }
                return iconEl;
            }

            protected void _render(RendererCellReference cell, String content, @Nullable Element icon) {
                Element span = DOM.createSpan();

                span.addClassName(V_CAPTIONTEXT_STYLENAME);
                span.setInnerSafeHtml(SafeHtmlUtils.fromSafeConstant(content));

                TableCellElement cellEl = cell.getElement();
                cellEl.removeAllChildren();

                if (icon != null) {
                    cellEl.appendChild(icon);
                }
                cellEl.appendChild(span);
            }

            private String getContentString(String htmlString) {
                switch (getState().mode) {
                    case HTML:
                        return htmlString;
                    case PREFORMATTED:
                        return "<pre>" + SafeHtmlUtils.htmlEscape(htmlString)
                                + "</pre>";
                    default:
                        return SafeHtmlUtils.htmlEscape(htmlString);
                }
            }
        };
    }
}
