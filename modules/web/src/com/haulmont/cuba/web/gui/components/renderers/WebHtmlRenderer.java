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
import com.haulmont.cuba.web.gui.components.WebAbstractDataGrid.AbstractRenderer;
import com.haulmont.cuba.web.sys.sanitizer.HtmlSanitizer;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.renderers.HtmlRenderer;

import javax.inject.Inject;

/**
 * A renderer for presenting HTML content.
 */
public class WebHtmlRenderer extends AbstractRenderer<Entity, String> implements DataGrid.HtmlRenderer {

    protected HtmlSanitizer htmlSanitizer;

    public WebHtmlRenderer() {
        this("");
    }

    public WebHtmlRenderer(String nullRepresentation) {
        super(nullRepresentation);
    }

    @Inject
    public void setHtmlSanitizer(HtmlSanitizer htmlSanitizer) {
        this.htmlSanitizer = htmlSanitizer;
    }

    @Override
    public HtmlRenderer getImplementation() {
        return (HtmlRenderer) super.getImplementation();
    }

    @Override
    protected HtmlRenderer createImplementation() {
        return new HtmlRenderer(getNullRepresentation());
    }

    @Override
    public String getNullRepresentation() {
        return super.getNullRepresentation();
    }

    @Override
    public void setNullRepresentation(String nullRepresentation) {
        super.setNullRepresentation(nullRepresentation);
    }

    @Override
    public ValueProvider<String, String> getPresentationValueProvider() {
        return (ValueProvider<String, String>) html ->
                getDataGrid().isHtmlSanitizerEnabled()
                        ? htmlSanitizer.sanitize(html)
                        : html;
    }
}
