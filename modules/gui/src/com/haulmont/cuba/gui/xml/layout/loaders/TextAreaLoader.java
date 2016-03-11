/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.TextArea;
import org.apache.commons.lang.StringUtils;

/**
 * @author abramov
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