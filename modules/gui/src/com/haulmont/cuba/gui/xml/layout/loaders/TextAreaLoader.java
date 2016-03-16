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

import com.haulmont.cuba.gui.components.TextArea;
import org.apache.commons.lang.StringUtils;

/**
 */
public class TextAreaLoader extends AbstractTextFieldLoader<TextArea> {

    @Override
    public void createComponent() {
        resultComponent = (TextArea) factory.createComponent(TextArea.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadMaxLength(resultComponent, element);
        loadTrimming(resultComponent, element);
        loadInputPrompt(resultComponent, element);

        String cols = element.attributeValue("cols");
        if (StringUtils.isNotEmpty(cols)) {
            resultComponent.setColumns(Integer.parseInt(cols));
        }

        String rows = element.attributeValue("rows");
        if (StringUtils.isNotEmpty(rows)) {
            resultComponent.setRows(Integer.parseInt(rows));
        }

        String wordwrap = element.attributeValue("wordwrap");
        if (StringUtils.isNotEmpty(wordwrap)) {
            resultComponent.setWordwrap(Boolean.parseBoolean(wordwrap));
        }
    }
}