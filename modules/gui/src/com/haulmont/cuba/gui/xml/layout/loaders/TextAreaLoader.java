/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
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