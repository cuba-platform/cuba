/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author subbotin
 * @version $Id$
 */
public class ResizableTextAreaLoader extends TextAreaLoader {

    public ResizableTextAreaLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Element element, Field field, Component parent) {
        super.initComponent(element, field, parent);

        if (field instanceof ResizableTextArea) {
            ResizableTextArea textArea = (ResizableTextArea) field;
            String resizable = element.attributeValue("resizable");

            if (StringUtils.isNotEmpty(resizable)) {
                textArea.setResizable(BooleanUtils.toBoolean(resizable));
            }
        }
    }
}