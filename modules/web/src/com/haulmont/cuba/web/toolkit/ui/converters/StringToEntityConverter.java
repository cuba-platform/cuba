/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.converters;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class StringToEntityConverter implements Converter<String, Entity> {
    @Override
    public Entity convertToModel(String value, Class<? extends Entity> targetType, Locale locale)
            throws ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(Entity value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (value != null)
            return InstanceUtils.getInstanceName(value);
        else
            return "";
    }

    @Override
    public Class<Entity> getModelType() {
        return Entity.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}