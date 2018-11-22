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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import org.dom4j.Element;

public class TextFieldLoader extends AbstractTextFieldLoader<TextField> {
    @Override
    public void loadComponent() {
        super.loadComponent();

        loadMaxLength(resultComponent, element);
        loadTrimming(resultComponent, element);

        loadDatatype(resultComponent, element);

        resultComponent.setFormatter(loadFormatter(element));

        loadInputPrompt(resultComponent, element);
        loadCaseConversion(resultComponent, element);
        loadTextChangeEventProperties(resultComponent, element);
        loadHtmlName(resultComponent, element);
    }

    @Override
    public void createComponent() {
        resultComponent = factory.create(TextField.NAME);
        loadId(resultComponent, element);
    }

    protected void loadHtmlName(TextInputField.HtmlNameSupported component, Element element) {
        String htmlName = element.attributeValue("htmlName");
        if (htmlName != null && !htmlName.isEmpty()) {
            component.setHtmlName(htmlName);
        }
    }
}