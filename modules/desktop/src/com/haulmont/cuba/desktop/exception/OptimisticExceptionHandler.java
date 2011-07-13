/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.openjpa.util.OptimisticException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class OptimisticExceptionHandler extends AbstractExceptionHandler<OptimisticException> {
    public OptimisticExceptionHandler() {
        super(OptimisticException.class);
    }

    @Override
    protected void doHandle(Thread thread, OptimisticException e) {
        Pattern pattern = Pattern.compile("\\[([^-]*)-");
        Matcher matcher = pattern.matcher(e.getMessage());
        String entityClassName = "";
        if (matcher.find()) {
            entityClassName = matcher.group(1);
        }

        String localizedEntityName;
        String entityName = entityClassName.substring(entityClassName.lastIndexOf(".") + 1);
        String packageName = entityClassName.substring(0, entityClassName.lastIndexOf("."));
        localizedEntityName = MessageProvider.getMessage(packageName, entityName);

        String msg = MessageProvider.formatMessage(getClass(), "optimisticException.message", "\"" + localizedEntityName + "\"");
        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
