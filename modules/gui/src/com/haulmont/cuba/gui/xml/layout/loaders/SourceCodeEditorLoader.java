/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.SourceCodeEditor;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 */
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
            resultComponent.setShowGutter(Boolean.valueOf(showGutter));
        }

        String printMargin = element.attributeValue("printMargin");
        if (StringUtils.isNotEmpty(printMargin)) {
            resultComponent.setShowPrintMargin(Boolean.valueOf(printMargin));
        }

        String highlightActiveLine = element.attributeValue("highlightActiveLine");
        if (StringUtils.isNotEmpty(highlightActiveLine)) {
            resultComponent.setHighlightActiveLine(Boolean.valueOf(highlightActiveLine));
        }

        String handleTabKey = element.attributeValue("handleTabKey");
        if (StringUtils.isNotEmpty("handleTabKey")) {
            resultComponent.setHandleTabKey(Boolean.valueOf(handleTabKey));
        }
    }

    protected void loadMode(SourceCodeEditor component, Element element) {
        String mode = element.attributeValue("mode");
        if (StringUtils.isNotEmpty(mode)) {
            component.setMode(SourceCodeEditor.Mode.parse(mode));
        }
    }
}