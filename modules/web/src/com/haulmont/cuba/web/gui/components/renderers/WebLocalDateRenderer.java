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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.web.gui.components.WebAbstractDataGrid.AbstractRenderer;
import com.vaadin.ui.renderers.LocalDateRenderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * A renderer for presenting LocalDate values.
 */
public class WebLocalDateRenderer extends AbstractRenderer<Entity, LocalDate> implements DataGrid.LocalDateRenderer {

    private Locale locale;
    private String formatPattern;
    private DateTimeFormatter formatter;

    public WebLocalDateRenderer() {
        super("");
        locale = AppBeans.get(UserSessionSource.class).getLocale();
    }

    public WebLocalDateRenderer(String formatPattern) {
        this(formatPattern, "");
    }

    public WebLocalDateRenderer(String formatPattern, String nullRepresentation) {
        this(formatPattern, AppBeans.get(UserSessionSource.class).getLocale(), nullRepresentation);
    }

    public WebLocalDateRenderer(String formatPattern, Locale locale) {
        this(formatPattern, locale, "");
    }

    public WebLocalDateRenderer(String formatPattern, Locale locale, String nullRepresentation) {
        super(nullRepresentation);

        this.formatPattern = formatPattern;
        this.locale = locale;
    }

    public WebLocalDateRenderer(DateTimeFormatter formatter) {
        this(formatter, "");
    }

    public WebLocalDateRenderer(DateTimeFormatter formatter, String nullRepresentation) {
        super(nullRepresentation);

        this.formatter = formatter;
    }

    @Override
    public LocalDateRenderer getImplementation() {
        return (LocalDateRenderer) super.getImplementation();
    }

    @Override
    protected LocalDateRenderer createImplementation() {
        if (formatter == null) {
            checkNotNullArgument(formatPattern, "Format pattern may not be null");
            checkNotNullArgument(locale, "Locale may not be null");
            formatter = DateTimeFormatter.ofPattern(formatPattern, locale);
        }
        return new LocalDateRenderer(formatter, getNullRepresentation());
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
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        checkRendererNotSet();
        this.locale = locale;
    }

    @Override
    public String getFormatPattern() {
        return formatPattern;
    }

    @Override
    public void setFormatPattern(String formatPattern) {
        checkRendererNotSet();
        this.formatPattern = formatPattern;
        this.formatter = null;
    }

    @Override
    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(DateTimeFormatter formatter) {
        checkRendererNotSet();
        this.formatter = formatter;
        this.formatPattern = null;
    }
}
