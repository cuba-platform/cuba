/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles a JPA optimistic lock exception.
 * <p/>
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class OptimisticExceptionHandler extends AbstractExceptionHandler {

    public OptimisticExceptionHandler() {
        super("org.springframework.orm.jpa.JpaOptimisticLockingFailureException", "org.apache.openjpa.persistence.OptimisticLockException");
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        Pattern pattern = Pattern.compile("\\[([^-]*)-");
        Matcher matcher = pattern.matcher(message);
        String localizedEntityName;
        if (matcher.find()) {
            String entityClassName = matcher.group(1);
            String entityName = entityClassName.substring(entityClassName.lastIndexOf(".") + 1);
            String packageName = entityClassName.substring(0, entityClassName.lastIndexOf("."));
            localizedEntityName = MessageProvider.getMessage(packageName, entityName);
        } else {
            localizedEntityName = "?";
        }

        String msg = MessageProvider.formatMessage(getClass(), "optimisticException.message", "\"" + localizedEntityName + "\"");
        App.getInstance().getMainFrame().showNotification(msg, IFrame.NotificationType.ERROR);
    }
}
