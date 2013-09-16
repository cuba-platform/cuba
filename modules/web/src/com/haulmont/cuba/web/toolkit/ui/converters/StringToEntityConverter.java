/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Formatter;
import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class StringToEntityConverter implements Converter<String, Entity> {

    protected Formatter formatter;

    @Override
    public Entity convertToModel(String value, Class<? extends Entity> targetType, Locale locale)
            throws ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(Entity value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (getFormatter() != null) {
            return getFormatter().format(value);
        }

        if (value != null) {
            return InstanceUtils.getInstanceName(value);
        }

        return "";
    }

    @Override
    public Class<Entity> getModelType() {
        return Entity.class;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}