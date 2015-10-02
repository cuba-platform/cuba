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
 * @version $Id$
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
    }

    protected void loadMode(SourceCodeEditor component, Element element) {
        String mode = element.attributeValue("mode");
        if (StringUtils.isNotEmpty(mode)) {
            component.setMode(SourceCodeEditor.Mode.parse(mode));
        }
    }
}