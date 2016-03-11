/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author subbotin
 * @version $Id$
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