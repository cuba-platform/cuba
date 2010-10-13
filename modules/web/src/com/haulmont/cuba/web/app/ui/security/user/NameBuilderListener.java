/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maxim Gorbunkov
 * Created: 24.03.2010 17:37:37
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.security.global.UserUtils;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.Field;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.Arrays;

public class NameBuilderListener extends DsListenerAdapter {
    private Field displayedNameField;
    private Field firstNameField;
    private Field lastNameField;
    private Field middleNameField;

    public NameBuilderListener(Window window) {
        displayedNameField = (Field) WebComponentsHelper.unwrap(window.getComponent("name"));
        firstNameField = (Field) WebComponentsHelper.unwrap(window.getComponent("firstName"));
        lastNameField = (Field) WebComponentsHelper.unwrap(window.getComponent("lastName"));
        middleNameField = (Field) WebComponentsHelper.unwrap(window.getComponent("middleName"));
    }

    public NameBuilderListener(FieldGroup fieldGroup) {
        com.haulmont.cuba.web.toolkit.ui.FieldGroup component =
                (com.haulmont.cuba.web.toolkit.ui.FieldGroup) WebComponentsHelper.unwrap(fieldGroup);
        displayedNameField = component.getField("name");
        firstNameField = component.getField("firstName");
        lastNameField = component.getField("lastName");
        middleNameField = component.getField("middleName");
    }

    @Override
    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
        if (!Arrays.asList("firstName", "lastName", "middleName").contains(property)) return;

        String firstName = (String) firstNameField.getValue();
        String lastName = (String) lastNameField.getValue();
        String middleName = (String) middleNameField.getValue();

        String displayedName;
        try{
            String pattern = AppContext.getProperty("cuba.user.fullNamePattern");
            if (StringUtils.isBlank(pattern))
                pattern = "{LL| }{F|. }{M|. }";

            displayedName = UserUtils.formatName(pattern, firstName, lastName, middleName);
        } catch (ParseException pe) {
            displayedName = "";
        }

//        Locale locale = App.getInstance().getLocale();
//
//        if (locale != null && "ru".equals(locale.getLanguage())) {
//            displayedName =
//                    (StringUtils.isNotEmpty(lastName) ? lastName : "") +
//                    (StringUtils.isNotEmpty(firstName) ? " " + firstName.substring(0, 1).toUpperCase() + "." : "") +
//                    (StringUtils.isNotEmpty(middleName) ? " " + middleName.substring(0, 1).toUpperCase() + "." : "");
//        } else {
//            displayedName =
//                    (StringUtils.isNotEmpty(firstName) ? firstName : "") +
//                    (StringUtils.isNotEmpty(middleName) ? " " + middleName.substring(0, 1).toUpperCase() + "." : "") +
//                    (StringUtils.isNotEmpty(lastName) ? " " + lastName : "");
//        }

        displayedNameField.setValue(displayedName);

    }
}
