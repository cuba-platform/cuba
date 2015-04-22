/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.IFrame;
import org.springframework.core.Ordered;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles a JPA optimistic lock exception.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_OptimisticExceptionHandler")
public class OptimisticExceptionHandler extends AbstractGenericExceptionHandler implements Ordered {

    @Inject
    protected Messages messages;

    public OptimisticExceptionHandler() {
        super("org.springframework.orm.jpa.JpaOptimisticLockingFailureException", "org.apache.openjpa.persistence.OptimisticLockException");
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        Pattern pattern = Pattern.compile("\\[([^-]*)-");
        Matcher matcher = pattern.matcher(message);
        String entityClassName = "";
        if (matcher.find()) {
            entityClassName = matcher.group(1);
        }

        String msg;

        if (entityClassName.contains(".")) {
            String entityName = entityClassName.substring(entityClassName.lastIndexOf(".") + 1);
            String packageName = entityClassName.substring(0, entityClassName.lastIndexOf("."));
            String localizedEntityName = messages.getMessage(packageName, entityName);

            msg = messages.formatMessage(messages.getMainMessagePack(),
                    "optimisticException.message", "\"" + localizedEntityName + "\"");
        } else {
            msg = messages.getMessage(messages.getMainMessagePack(), "optimisticExceptionUnknownObject.message");
        }
        windowManager.showNotification(msg, IFrame.NotificationType.ERROR);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 40;
    }
}