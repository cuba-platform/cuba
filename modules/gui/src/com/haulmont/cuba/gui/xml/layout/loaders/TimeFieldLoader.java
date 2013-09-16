/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.xml.layout.*;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TimeFieldLoader extends AbstractFieldLoader {

    public TimeFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        TimeField timeField = (TimeField) super.loadComponent(factory, element, parent);

        String s = element.attributeValue("showSeconds");
        if (s == null) {
            return timeField;
        } else if (Boolean.valueOf(s)) {
            timeField.setShowSeconds(true);
        } else
            timeField.setShowSeconds(false);

        return timeField;
    }

    @Override
    protected void loadValidators(Field component, Element element) {
        // don't load any validators
    }
}
