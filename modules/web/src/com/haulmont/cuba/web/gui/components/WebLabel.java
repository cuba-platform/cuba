/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.web.widgets.CubaLabel;
import com.vaadin.shared.ui.ContentMode;

import javax.inject.Inject;

public class WebLabel<V> extends WebAbstractViewComponent<com.vaadin.ui.Label, String, V> implements Label<V> {

    @Inject
    protected MetadataTools metadataTools;

    protected Formatter<? super V> formatter;

    public WebLabel() {
        component = new CubaLabel();
        component.setSizeUndefined();
    }

    @Override
    protected void setValueToPresentation(String value) {
        if (hasValidationError()) {
            setValidationError(null);
        }

        component.setValue(value);
    }

    protected String convertToPresentation(V modelValue) {
        if (formatter != null) {
            return formatter.format(modelValue);
        }

        if (valueBinding != null
                && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            MetaProperty metaProperty = entityValueSource.getMetaPropertyPath().getMetaProperty();
            return metadataTools.format(modelValue, metaProperty);
        }

        return metadataTools.format(modelValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Formatter<V> getFormatter() {
        return (Formatter<V>) formatter;
    }

    @Override
    public void setFormatter(Formatter<? super V> formatter) {
        this.formatter = formatter;
    }

    @Override
    public boolean isHtmlEnabled() {
        return component.getContentMode() == ContentMode.HTML;
    }

    @Override
    public void setHtmlEnabled(boolean htmlEnabled) {
        component.setContentMode(htmlEnabled ? ContentMode.HTML : ContentMode.TEXT);
    }

    @Override
    public String getRawValue() {
        return component.getValue();
    }
}