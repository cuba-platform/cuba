/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
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

    @Override
    protected void initComponent(Field field, Element element, Component parent) {
        super.initComponent(field, element, parent);

        if (field instanceof MaskedField) {
            MaskedField component = (MaskedField) field;
            String mask = element.attributeValue("mask");
            if (!StringUtils.isEmpty(mask)) {
                component.setMask(loadResourceString(mask));
            }
            String valueModeStr = element.attributeValue("valueMode", MaskedField.ValueMode.CLEAR.getId());
            component.setValueMode(MaskedField.ValueMode.fromId(valueModeStr));
        }
    }
}
