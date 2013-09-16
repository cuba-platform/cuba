/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class TextFieldLoader extends AbstractTextFieldLoader {

    private static final Log log = LogFactory.getLog(TextFieldLoader.class);

    public TextFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        if (element.attribute("rows") != null || element.attribute("cols") != null)
            log.warn("For textField element specified rows or cols attribute, use textArea for this purpose");
        if (element.attribute("secret") != null)
            log.warn("For textField element specified secret attribute, use passwordField for this purpose");

        TextField component = (TextField) super.loadComponent(factory, element, parent);

        loadMaxLength(element, component);
        loadTrimming(element, component);

        String datatypeStr = element.attributeValue("datatype");
        if (!StringUtils.isEmpty(datatypeStr)) {
            Datatype datatype = Datatypes.get(datatypeStr);
            component.setDatatype(datatype);
        }

        component.setFormatter(loadFormatter(element));
        return component;
    }
}