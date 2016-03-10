/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles database unique constraint violations. Determines the exception type by searching a special marker string
 * in the messages of all exceptions in the chain.
 *
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_UniqueConstraintViolationHandler")
public class UniqueConstraintViolationHandler implements GenericExceptionHandler, Ordered {

    @Inject
    protected Messages messages;

    @Inject
    protected ClientConfig clientConfig;

    @Override
    public boolean handle(Throwable exception, WindowManager windowManager) {
        Throwable t = exception;
        try {
            while (t != null) {
                if (t.toString().contains("org.eclipse.persistence.exceptions.DatabaseException")) {
                    return doHandle(t, windowManager);
                }
                t = t.getCause();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean doHandle(Throwable throwable, WindowManager windowManager) {
        Pattern pattern = clientConfig.getUniqueConstraintViolationPattern();
        String constraintName = "";

        Matcher matcher = pattern.matcher(throwable.toString());
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                constraintName = matcher.group(1);
            } else {
                for (int i = 1; i > matcher.groupCount(); i++) {
                    if (StringUtils.isNotBlank(matcher.group(i))) {
                        constraintName = matcher.group(i);
                        break;
                    }
                }
            }

            String msg = "";
            if (StringUtils.isNotBlank(constraintName)) {
                msg = messages.getMainMessage(constraintName.toUpperCase());
            }

            if (msg.equalsIgnoreCase(constraintName)) {
                msg = messages.getMainMessage("uniqueConstraintViolation.message");
                if (StringUtils.isNotBlank(constraintName)) {
                    msg = msg + " (" + constraintName + ")";
                }
            }

            windowManager.showNotification(msg, Frame.NotificationType.ERROR);
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 60;
    }
}