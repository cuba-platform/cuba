/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 21.01.2010 10:07:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.persistence.OptimisticLockException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JPAOptimisticExceptionHandler extends AbstractExceptionHandler<OptimisticLockException> {

    public JPAOptimisticExceptionHandler() {
        super(OptimisticLockException.class);
    }

    protected void doHandle(OptimisticLockException e, App app) {
        Pattern pattern = Pattern.compile("\\[([^-]*)-");
        Matcher matcher = pattern.matcher(ExceptionUtils.getStackTrace(e));
        String entityClassName = "";
        if (matcher.find()) {
            entityClassName = matcher.group(1);
        }

        String localizedEntityName = "";
        String entityName = entityClassName.substring(entityClassName.lastIndexOf(".") + 1);
        String packageName = entityClassName.substring(0, entityClassName.lastIndexOf("."));
        localizedEntityName = MessageProvider.getMessage(packageName, entityName);

        String msg = MessageProvider.formatMessage(getClass(), "optimisticException.message", "\"" + localizedEntityName + "\"");
        app.getAppWindow().showNotification(msg, Window.Notification.TYPE_ERROR_MESSAGE);
    }
}
