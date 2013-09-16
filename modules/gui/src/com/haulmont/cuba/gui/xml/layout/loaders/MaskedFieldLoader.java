/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.MaskedField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author devyatkin
 * @version $Id$
 */
public class MaskedFieldLoader extends AbstractTextFieldLoader {
    public MaskedFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Component component = super.loadComponent(factory, element, parent);
        if (component instanceof MaskedField) {
            MaskedField field = (MaskedField) component;
            String mask = element.attributeValue("mask");
            if (!StringUtils.isEmpty(mask)) {
                field.setMask(loadResourceString(mask));
            }
            String valueModeStr = element.attributeValue("valueMode", MaskedField.ValueMode.CLEAR.getId());
            field.setValueMode(MaskedField.ValueMode.fromId(valueModeStr));
        }
        return component;
    }
}
