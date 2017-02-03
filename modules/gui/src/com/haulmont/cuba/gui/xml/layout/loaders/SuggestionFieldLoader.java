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

import com.haulmont.cuba.gui.components.SuggestionField;

public class SuggestionFieldLoader extends AbstractFieldLoader<SuggestionField> {

    @Override
    public void createComponent() {
        resultComponent = (SuggestionField) factory.createComponent(SuggestionField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadAsyncSearchDelayMs();
        loadMinSearchStringLength();
        loadSuggestionsLimit();
    }

    private void loadSuggestionsLimit() {
        if (element.attribute("suggestionsLimit") != null) {
            String suggestionsLimit = element.attributeValue("suggestionsLimit");
            resultComponent.setSuggestionsLimit(Integer.parseInt(suggestionsLimit));
        }
    }

    private void loadMinSearchStringLength() {
        if (element.attribute("minSearchStringLength") != null) {
            String minSearchStringLength = element.attributeValue("minSearchStringLength");
            resultComponent.setMinSearchStringLength(Integer.parseInt(minSearchStringLength));
        }
    }

    private void loadAsyncSearchDelayMs() {
        if (element.attribute("asyncSearchDelayMs") != null) {
            String asyncSearchDelayMs = element.attributeValue("asyncSearchDelayMs");
            resultComponent.setAsyncSearchDelayMs(Integer.parseInt(asyncSearchDelayMs));
        }
    }
}
