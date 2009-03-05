/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 14:10:16
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.core.global.MetadataProvider;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class PickerFieldLoader extends AbstractFieldLoader{
    public PickerFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final PickerField component = (PickerField) super.loadComponent(factory, element);
        final String metaClass = element.attributeValue("metaClass");

        if (!StringUtils.isEmpty(metaClass)) {
            component.setMetaClass(MetadataProvider.getSession().getClass(metaClass));
        }

        return component;
    }
}
