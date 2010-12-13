/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 09.12.2010 17:19:29
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.xml.layout.*;
import org.dom4j.Element;

public class TimeFieldLoader extends AbstractFieldLoader {

    public TimeFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        TimeField timeField = (TimeField) super.loadComponent(factory, element, parent);

        String s = element.attributeValue("showSeconds");
        if (Boolean.valueOf(s)) {
            timeField.setShowSeconds(true);
        }

        return timeField;
    }

    @Override
    protected void loadValidators(Field component, Element element) {
        // don't load any validators
    }
}
