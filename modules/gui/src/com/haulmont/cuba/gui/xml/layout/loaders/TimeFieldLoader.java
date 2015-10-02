/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.TimeField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TimeFieldLoader extends AbstractFieldLoader<TimeField> {

    @Override
    public void createComponent() {
        resultComponent = (TimeField) factory.createComponent(TimeField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        String timeFormat = element.attributeValue("timeFormat");
        if (StringUtils.isNotEmpty(timeFormat)) {
            timeFormat = loadResourceString(timeFormat);
            resultComponent.setFormat(timeFormat);
        }

        String showSeconds = element.attributeValue("showSeconds");
        if (StringUtils.isNotEmpty(showSeconds)) {
            resultComponent.setShowSeconds(Boolean.valueOf(showSeconds));
        }
    }

    @Override
    protected void loadValidators(Field component, Element element) {
        // don't load any validators
    }
}