/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 11:13:20
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextField;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;

public class TextFieldLoader extends AbstractFieldLoader {
    public TextFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final TextField component = (TextField) super.loadComponent(factory, element);

        final String cols = element.attributeValue("cols");
        final String rows = element.attributeValue("rows");

        if (!StringUtils.isEmpty(cols)) {
            component.setColumns(Integer.valueOf(cols));
        }
        if (!StringUtils.isEmpty(rows)) {
            component.setRows(Integer.valueOf(rows));
        }

        String secret = element.attributeValue("secret");
        if (!StringUtils.isEmpty(secret)) {
            component.setSecret(Boolean.valueOf(secret));
        }

        return component;
    }
}
