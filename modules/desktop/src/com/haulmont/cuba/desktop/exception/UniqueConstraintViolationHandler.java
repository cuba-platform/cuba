/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.lang.StringUtils;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class UniqueConstraintViolationHandler implements ExceptionHandler {

    private String marker;

    private Pattern pattern;

    private String getMarker() {
        if (marker == null) {
            marker = ServiceLocator.getDataService().getDbDialect().getUniqueConstraintViolationMarker();
        }
        return marker;
    }

    private Pattern getPattern() {
        if (pattern == null) {
            String s = ServiceLocator.getDataService().getDbDialect().getUniqueConstraintViolationPattern();
            pattern = Pattern.compile(s);
        }
        return pattern;
    }

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        Throwable t = exception;
        try {
            while (t != null) {
                if (t.getMessage() != null && t.getMessage().contains(getMarker())) {
                    doHandle(thread, t);
                    return true;
                }
                t = t.getCause();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected void doHandle(Thread thread, Throwable e) {
        String constraintName = "";
        Matcher matcher = getPattern().matcher(e.getMessage());
        if (matcher.find()) {
            if (matcher.groupCount() > 1)
                constraintName = matcher.group(2);
            else
                constraintName = matcher.group(1);
        }

        String msg = "";
        if (StringUtils.isNotBlank(constraintName)) {
            msg = MessageProvider.getMessage(MessageUtils.getMessagePack(), constraintName.toUpperCase());
        }

        if (msg.equalsIgnoreCase(constraintName)) {
            msg = MessageProvider.getMessage(getClass(), "uniqueConstraintViolation.message");
            if (StringUtils.isNotBlank(constraintName))
                msg = msg + " (" + constraintName + ")";
        }

        App.getInstance().showNotificationPopup(msg, IFrame.NotificationType.ERROR);
    }
}
