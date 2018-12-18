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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Map;

public class DesktopCurrencyField extends DesktopTextField implements CurrencyField {

    //just stub
    protected String currency;
    //just stub
    protected boolean showCurrencyLabel = true;
    //just stub
    protected CurrencyLabelPosition currencyLabelPosition = CurrencyLabelPosition.RIGHT;

    @Override
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public void setShowCurrencyLabel(boolean showCurrencyLabel) {
        this.showCurrencyLabel = showCurrencyLabel;
    }

    @Override
    public boolean getShowCurrencyLabel() {
        return showCurrencyLabel;
    }

    @Override
    public void setCurrencyLabelPosition(CurrencyLabelPosition currencyLabelPosition) {
        Preconditions.checkNotNullArgument(currencyLabelPosition);

        this.currencyLabelPosition = currencyLabelPosition;
    }

    @Override
    public CurrencyLabelPosition getCurrencyLabelPosition() {
        return currencyLabelPosition;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);

        if (datasource != null && !DynamicAttributesUtils.isDynamicAttribute(property)) {
            MetaProperty metaProperty = datasource.getMetaClass().getPropertyNN(property);

            Object annotation = metaProperty.getAnnotations().get(CurrencyValue.class.getName());
            if (annotation == null)
                return;

            //noinspection unchecked
            Map<String, Object> annotationProperties = (Map<String, Object>) annotation;

            currency = (String) annotationProperties.get("currency");

            String labelPosition = ((com.haulmont.cuba.core.entity.annotation.CurrencyLabelPosition) annotationProperties.get("labelPosition")).name();
            currencyLabelPosition = CurrencyLabelPosition.valueOf(labelPosition);
        }
    }
}
