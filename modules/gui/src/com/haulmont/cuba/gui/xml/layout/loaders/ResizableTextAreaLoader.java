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

import com.haulmont.cuba.gui.components.ResizableTextArea;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 */
public class ResizableTextAreaLoader extends TextAreaLoader {
    @Override
    public void createComponent() {
        resultComponent = (ResizableTextArea) factory.createComponent(ResizableTextArea.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        ResizableTextArea textArea = (ResizableTextArea) resultComponent;
        String resizable = element.attributeValue("resizable");

        if (StringUtils.isNotEmpty(resizable)) {
            textArea.setResizable(Boolean.parseBoolean(resizable));
        }
    }
}