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

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.haulmont.cuba.gui.components.ResizableTextArea.ResizeDirection;

public class ResizableTextAreaLoader extends TextAreaLoader {

    private static final Logger log = LoggerFactory.getLogger(ResizableTextAreaLoader.class);

    @Override
    public void createComponent() {
        if (element.getName().equals(ResizableTextArea.NAME)) {
            resultComponent = (ResizableTextArea) factory.createComponent(ResizableTextArea.NAME);
        }

        if (element.getName().equals(TextArea.NAME)) {
            if (isResizable() || hasResizableDirection()) {
                resultComponent = (ResizableTextArea) factory.createComponent(ResizableTextArea.NAME);
                log.warn("The 'resizableTextArea' element must be used in order to create a resizable text area " +
                        "instead of 'textArea'");
            } else {
                resultComponent = (TextArea) factory.createComponent(TextArea.NAME);
            }
        }

        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        if (resultComponent instanceof ResizableTextArea) {
            ResizableTextArea textArea = (ResizableTextArea) resultComponent;
            String resizable = element.attributeValue("resizable");
            if (!Strings.isNullOrEmpty(resizable)) {
                textArea.setResizable(Boolean.parseBoolean(resizable));
            }

            String resizableDirection = element.attributeValue("resizableDirection");
            if (!Strings.isNullOrEmpty(resizableDirection)) {
                textArea.setResizableDirection(ResizeDirection.valueOf(resizableDirection));
            }

            loadSettingsEnabled(textArea, element);
        }
    }

    protected boolean isResizable() {
        String resizable = element.attributeValue("resizable");
        if (!Strings.isNullOrEmpty(resizable)) {
            return Boolean.parseBoolean(resizable);
        }

        return false;
    }

    protected boolean hasResizableDirection() {
        String resizableDirection = element.attributeValue("resizableDirection");
        if (!Strings.isNullOrEmpty(resizableDirection)) {
            return ResizeDirection.valueOf(resizableDirection) != ResizeDirection.NONE;
        }

        return false;
    }
}