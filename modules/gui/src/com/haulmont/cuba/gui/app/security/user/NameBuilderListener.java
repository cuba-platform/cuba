/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.user;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.security.global.UserUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class NameBuilderListener<T extends Entity> extends DsListenerAdapter<T> {

    private static final List<String> PROPERTY_NAMES = Arrays.asList("firstName", "lastName", "middleName");

    private Window window;
    private FieldGroup fieldGroup;
    private String pattern;

    public NameBuilderListener(Window window) {
        if (window == null)
            throw new IllegalArgumentException("window is null");

        this.window = window;
    }

    public NameBuilderListener(Window window, String pattern) {
        if (window == null)
            throw new IllegalArgumentException("window is null");

        this.window = window;
        this.pattern = pattern;
    }

    public NameBuilderListener(FieldGroup fieldGroup) {
        if (fieldGroup == null)
            throw new IllegalArgumentException("fieldGroup is null");

        this.fieldGroup = fieldGroup;
    }

    @Override
    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
        if (!PROPERTY_NAMES.contains(property))
            return;

        String firstName = getFieldValue("firstName");
        String lastName = getFieldValue("lastName");
        String middleName = getFieldValue("middleName");

        String displayedName;
        try {
            if (this.pattern == null) {
                pattern = AppContext.getProperty("cuba.user.fullNamePattern");
                if (StringUtils.isBlank(pattern))
                    pattern = "{LL| }{F|. }{M|. }";
            }

            displayedName = UserUtils.formatName(pattern, firstName, lastName, middleName);
        } catch (ParseException pe) {
            displayedName = "";
        }

        if (window != null) {
            Field field = window.getComponent("name");
            field.setValue(displayedName);
        } else {
            fieldGroup.setFieldValue("name", displayedName);
        }
    }

    private String getFieldValue(String name) {
        if (window != null) {
            Field field = window.getComponent(name);
            return field.getValue();
        } else {
            return (String) fieldGroup.getFieldValue(name);
        }
    }
}
