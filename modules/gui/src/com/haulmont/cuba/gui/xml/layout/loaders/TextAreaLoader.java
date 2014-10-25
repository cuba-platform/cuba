/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
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
    protected void initComponent(Field field, Element element, Component parent) {
        super.initComponent(field, element, parent);

        TextArea component = (TextArea) field;

        loadMaxLength(element, component);
        loadTrimming(element, component);

        final String cols = element.attributeValue("cols");
        if (StringUtils.isNotEmpty(cols)) {
            component.setColumns(Integer.parseInt(cols));
        }

        final String rows = element.attributeValue("rows");
        if (StringUtils.isNotEmpty(rows)) {
            component.setRows(Integer.parseInt(rows));
        }
    }
}