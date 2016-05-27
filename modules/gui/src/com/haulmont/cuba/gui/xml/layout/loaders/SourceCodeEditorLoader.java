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

import com.haulmont.cuba.gui.components.SourceCodeEditor;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class SourceCodeEditorLoader extends AbstractFieldLoader<SourceCodeEditor> {
    @Override
    public void createComponent() {
        resultComponent = (SourceCodeEditor) factory.createComponent(SourceCodeEditor.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadMode(resultComponent, element);

        String showGutter = element.attributeValue("showGutter");
        if (StringUtils.isNotEmpty(showGutter)) {
            resultComponent.setShowGutter(Boolean.parseBoolean(showGutter));
        }

        String printMargin = element.attributeValue("printMargin");
        if (StringUtils.isNotEmpty(printMargin)) {
            resultComponent.setShowPrintMargin(Boolean.parseBoolean(printMargin));
        }

        String highlightActiveLine = element.attributeValue("highlightActiveLine");
        if (StringUtils.isNotEmpty(highlightActiveLine)) {
            resultComponent.setHighlightActiveLine(Boolean.parseBoolean(highlightActiveLine));
        }

        String handleTabKey = element.attributeValue("handleTabKey");
        if (StringUtils.isNotEmpty("handleTabKey")) {
            resultComponent.setHandleTabKey(Boolean.parseBoolean(handleTabKey));
        }
    }

    protected void loadMode(SourceCodeEditor component, Element element) {
        String mode = element.attributeValue("mode");
        if (StringUtils.isNotEmpty(mode)) {
            component.setMode(SourceCodeEditor.Mode.parse(mode));
        }
    }
}