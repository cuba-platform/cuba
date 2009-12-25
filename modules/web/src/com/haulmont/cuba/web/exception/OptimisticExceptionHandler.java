/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: FIRSTNAME LASTNAME
 * Created: 25.12.2009 10:22:36
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;
import org.apache.openjpa.util.OptimisticException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptimisticExceptionHandler extends AbstractExceptionHandler<OptimisticException>{
    public OptimisticExceptionHandler() {
        super(OptimisticException.class);
    }

    @Override
    protected void doHandle(OptimisticException e, App app) {
        Pattern pattern = Pattern.compile("\\[([^-]*)-");
        Matcher matcher = pattern.matcher(e.getMessage());
        String entityClassName = "";
        if (matcher.find()) {
            entityClassName = matcher.group(1);
        }

        String localizedEntityName = "";
        try {
            String entityName = entityClassName.substring(entityClassName.lastIndexOf(".") + 1);
            localizedEntityName = MessageProvider.getMessage(Class.forName(entityClassName), entityName);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        
        String msg = MessageProvider.getMessage(getClass(), "optimisticException.message");
        msg = msg.replace("$1", "\"" + localizedEntityName + "\"");
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }
}
