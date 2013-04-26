/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class TextAreaLoader extends AbstractTextFieldLoader {
    public TextAreaLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException {

        final TextArea component = (TextArea) super.loadComponent(factory, element, parent);

        loadMaxLength(element, component);
        loadTrimming(element, component);

        final String cols = element.attributeValue("cols");
        final String rows = element.attributeValue("rows");

        if (!StringUtils.isEmpty(cols)) {
            component.setColumns(Integer.valueOf(cols));
        }
        if (!StringUtils.isEmpty(rows)) {
            component.setRows(Integer.valueOf(rows));
        }

        return component;
    }
}