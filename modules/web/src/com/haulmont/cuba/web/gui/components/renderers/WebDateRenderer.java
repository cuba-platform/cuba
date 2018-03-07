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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.web.gui.components.WebDataGrid.AbstractRenderer;
import com.vaadin.v7.ui.renderers.DateRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * A renderer for presenting date values.
 */
public class WebDateRenderer extends AbstractRenderer<Date> implements DataGrid.DateRenderer {

    private Locale locale;
    private String formatString;
    private DateFormat dateFormat;

    public WebDateRenderer() {
        super("");
        locale = AppBeans.get(UserSessionSource.class).getLocale();
    }

    public WebDateRenderer(String formatString) {
        this(formatString, "");
    }

    public WebDateRenderer(String formatString, String nullRepresentation) {
        this(formatString, AppBeans.get(UserSessionSource.class).getLocale(), nullRepresentation);
    }

    public WebDateRenderer(String formatString, Locale locale) {
        this(formatString, locale, "");
    }

    public WebDateRenderer(String formatString, Locale locale, String nullRepresentation) {
        super(nullRepresentation);

        this.formatString = formatString;
        this.locale = locale;
    }

    public WebDateRenderer(DateFormat dateFormat) {
        this(dateFormat, "");
    }

    public WebDateRenderer(DateFormat dateFormat, String nullRepresentation) {
        super(nullRepresentation);

        this.dateFormat = dateFormat;
    }

    @Override
    public DateRenderer getImplementation() {
        return (DateRenderer) super.getImplementation();
    }

    @Override
    protected DateRenderer createImplementation() {
        if (dateFormat == null) {
            checkNotNullArgument(formatString, "Format string may not be null");
            checkNotNullArgument(locale, "Locale may not be null");
            dateFormat = new SimpleDateFormat(formatString, locale);
        }
        return new DateRenderer(dateFormat, getNullRepresentation());
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
    public String getFormatString() {
        return formatString;
    }

    @Override
    public void setFormatString(String formatString) {
        checkRendererNotSet();
        this.formatString = formatString;
        this.dateFormat = null;
    }

    @Override
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public void setDateFormat(DateFormat dateFormat) {
        checkRendererNotSet();
        this.dateFormat = dateFormat;
        this.formatString = null;
    }
}
