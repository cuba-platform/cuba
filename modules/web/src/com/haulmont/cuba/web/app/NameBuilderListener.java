/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maxim Gorbunkov
 * Created: 24.03.2010 17:37:37
 *
 * $Id$
 */
package com.haulmont.cuba.web.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.web.App;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Locale;

public class NameBuilderListener extends DsListenerAdapter {
    private TextField displayedNameField;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField middleNameField;

    public NameBuilderListener(Window window) {
        displayedNameField = window.getComponent("name");
        firstNameField = window.getComponent("firstName");
        lastNameField = window.getComponent("lastName");
        middleNameField = window.getComponent("middleName");
    }

    @Override
    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
        if (!Arrays.asList("firstName", "lastName", "middleName").contains(property)) return;

        String firstName = firstNameField.getValue();
        String lastName = lastNameField.getValue();
        String middleName = middleNameField.getValue();

        String displayedName;

        Locale locale = App.getInstance().getLocale();

        if (locale != null && "ru".equals(locale.getLanguage())) {
            displayedName =
                    (StringUtils.isNotEmpty(lastName) ? lastName : "") +
                    (StringUtils.isNotEmpty(firstName) ? " " + firstName.substring(0, 1).toUpperCase() + "." : "") +
                    (StringUtils.isNotEmpty(middleName) ? " " + middleName.substring(0, 1).toUpperCase() + "." : "");
        } else {
            displayedName =
                    (StringUtils.isNotEmpty(firstName) ? firstName : "") +
                    (StringUtils.isNotEmpty(middleName) ? " " + middleName.substring(0, 1).toUpperCase() + "." : "") +
                    (StringUtils.isNotEmpty(lastName) ? " " + lastName : "");
        }

        displayedNameField.setValue(displayedName);

    }
}
