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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SuggestionPickerField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class SuggestionPickerFieldLoader extends AbstractFieldLoader<SuggestionPickerField> {

    @Override
    public void createComponent() {
        resultComponent = (SuggestionPickerField) factory.createComponent(SuggestionPickerField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadTabIndex(resultComponent, element);

        loadMetaClass(resultComponent, element);

        loadCaptionProperty(resultComponent, element);

        loadActions(resultComponent);
        loadValidators(resultComponent, element);

        loadAsyncSearchDelayMs(resultComponent, element);
        loadMinSearchStringLength(resultComponent, element);
        loadSuggestionsLimit(resultComponent, element);
    }

    protected void loadCaptionProperty(SuggestionPickerField searchPickerField, Element element) {
        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            searchPickerField.setCaptionMode(CaptionMode.PROPERTY);
            searchPickerField.setCaptionProperty(captionProperty);
        }
    }

    protected void loadActions(SuggestionPickerField searchPickerField) {
        loadActions(searchPickerField, element);
        if (searchPickerField.getActions().isEmpty()) {
            searchPickerField.addLookupAction();
            searchPickerField.addOpenAction();
        }
    }

    protected void loadMetaClass(SuggestionPickerField searchPickerField, Element element) {
        String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            Metadata metadata = AppBeans.get(Metadata.NAME);
            searchPickerField.setMetaClass(metadata.getSession().getClass(metaClass));
        }
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        return loadPickerDeclarativeAction(actionsHolder, element);
    }

    protected void loadSuggestionsLimit(SuggestionPickerField suggestionField, Element element) {
        String suggestionsLimit = element.attributeValue("suggestionsLimit");
        if (StringUtils.isNotEmpty(suggestionsLimit)) {
            suggestionField.setSuggestionsLimit(Integer.parseInt(suggestionsLimit));
        }
    }

    protected void loadMinSearchStringLength(SuggestionPickerField suggestionField, Element element) {
        String minSearchStringLength = element.attributeValue("minSearchStringLength");
        if (StringUtils.isNotEmpty(minSearchStringLength)) {
            suggestionField.setMinSearchStringLength(Integer.parseInt(minSearchStringLength));
        }
    }

    protected void loadAsyncSearchDelayMs(SuggestionPickerField suggestionField, Element element) {
        String asyncSearchDelayMs = element.attributeValue("asyncSearchDelayMs");
        if (StringUtils.isNotEmpty(asyncSearchDelayMs)) {
            suggestionField.setAsyncSearchDelayMs(Integer.parseInt(asyncSearchDelayMs));
        }
    }
}
