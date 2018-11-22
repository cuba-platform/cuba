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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.Map;

public class CurrencyFieldLoader extends AbstractFieldLoader<CurrencyField> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(CurrencyField.class);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadCurrency(resultComponent, element);
        loadShowCurrencyLabel(resultComponent, element);
        loadCurrencyLabelPosition(resultComponent, element);
        loadDatatype(resultComponent, element);
        loadBuffered(resultComponent, element);
    }

    protected void loadCurrencyLabelPosition(CurrencyField resultComponent, Element element) {
        String currencyLabelPosition = element.attributeValue("currencyLabelPosition");
        if (StringUtils.isNotEmpty(currencyLabelPosition)) {
            resultComponent.setCurrencyLabelPosition(CurrencyField.CurrencyLabelPosition.valueOf(currencyLabelPosition));
        }
    }

    protected void loadShowCurrencyLabel(CurrencyField resultComponent, Element element) {
        String showCurrency = element.attributeValue("showCurrencyLabel");
        if (StringUtils.isNotEmpty(showCurrency)) {
            resultComponent.setShowCurrencyLabel(Boolean.parseBoolean(showCurrency));
        }
    }

    protected void loadCurrency(CurrencyField resultComponent, Element element) {
        String currency = element.attributeValue("currency");
        if (StringUtils.isNotEmpty(currency)) {
            resultComponent.setCurrency(currency);
        }
    }

    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        super.loadDatasource(component, element);

        if (component.getDatasource() == null)
            return;

        Map<String, Object> annotations = component.getMetaPropertyPath().getMetaProperty().getAnnotations();
        Object obj = annotations.get(CurrencyValue.class.getName());
        if (obj == null)
            return;

        //noinspection unchecked
        Map<String, Object> currencyValue = (Map<String, Object>) obj;
        String currencyName = (String) currencyValue.get("currency");
        if (StringUtils.isNotEmpty(currencyName)) {
            ((CurrencyField) component).setCurrency(currencyName);
        }
    }
}
