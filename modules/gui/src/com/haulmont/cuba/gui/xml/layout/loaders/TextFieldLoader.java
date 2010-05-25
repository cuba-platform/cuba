/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 11:13:20
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class TextFieldLoader extends AbstractFieldLoader {
    public TextFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final TextField component = (TextField) super.loadComponent(factory, element, parent);

        final String cols = element.attributeValue("cols");
        final String rows = element.attributeValue("rows");
        final String maxLength = element.attributeValue("maxLength");

        loadStyleName(component, element);

        if (!StringUtils.isEmpty(cols)) {
            component.setColumns(Integer.valueOf(cols));
        }
        if (!StringUtils.isEmpty(rows)) {
            component.setRows(Integer.valueOf(rows));
        }
        if (!StringUtils.isEmpty(maxLength)) {
            component.setMaxLength(Integer.valueOf(maxLength));
        }

        String secret = element.attributeValue("secret");
        if (!StringUtils.isEmpty(secret)) {
            component.setSecret(Boolean.valueOf(secret));
        }

        String datatypeStr = element.attributeValue("datatype");
        if (!StringUtils.isEmpty(datatypeStr)) {
            Datatype datatype = Datatypes.getInstance().get(datatypeStr);
            component.setDatatype(datatype);
        }

        return component;
    }
}
