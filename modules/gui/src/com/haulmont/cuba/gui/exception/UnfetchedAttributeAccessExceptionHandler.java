/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Konstantin Krivopustov
 * @version $Id$
 */
@Component("cuba_UnfetchedAttributeAccessExceptionHandler")
public class UnfetchedAttributeAccessExceptionHandler extends AbstractGenericExceptionHandler {

    private static final Pattern PATTERN = Pattern.compile("at (.+)\\._persistence_get_(.+)\\(");

    public UnfetchedAttributeAccessExceptionHandler() {
        super("com.haulmont.cuba.core.global.IllegalEntityStateException",
                "org.eclipse.persistence.exceptions.ValidationException");
    }

    @Override
    protected boolean canHandle(String className, String message, @Nullable Throwable throwable) {
        return className.equals("com.haulmont.cuba.core.global.IllegalEntityStateException")
                || (className.equals("org.eclipse.persistence.exceptions.ValidationException")
                    && message.contains("An attempt was made to traverse a relationship using indirection that had a null Session"));
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        String msg = "It usually occurs when the attribute is not included into a view";
        String defaultMsg = "\n\nSee the log to find out what attribute caused the exception";

        if (throwable != null) {
            Matcher matcher = PATTERN.matcher(ExceptionUtils.getStackTrace(throwable));
            if (matcher.find()) {
                msg += "\n\nEntity: " + matcher.group(1) + "\nAttribute: " + matcher.group(2);
            } else {
                msg += defaultMsg;
            }
        } else {
            msg += defaultMsg;
        }

        windowManager.showNotification("Unfetched attribute access error", msg, Frame.NotificationType.ERROR);
    }
}
