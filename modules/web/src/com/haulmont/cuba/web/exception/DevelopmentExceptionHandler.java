/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.log.DevelopmentExceptionWindow;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * @author hasanov
 * @version $Id$
 */
public class DevelopmentExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(ErrorEvent event, App app) {
        String msg = AppBeans.get(Messages.class).formatMessage(getClass(), "developmentExcepton.message");
        String stackTrace = ExceptionUtils.getStackTrace(event.getThrowable());
        AbstractComponent component = DefaultErrorHandler.findAbstractComponent(event);

        StringBuilder sb = new StringBuilder();
        sb.append(DevelopmentException.class.getName());
        sb.append(":  ");
        sb.append("<b>");
        sb.append(StringEscapeUtils.escapeHtml(msg));
        sb.append("</b>");
        sb.append(" in: ");
        sb.append(component.getClass().getName());
        sb.append(":");
        if (stackTrace != null) {
            sb.append(" ");
            sb.append(StringUtils.replace(StringEscapeUtils.escapeHtml(stackTrace), "\n", "<br/>"));
        }
        sb.append("<br/>");

        DevelopmentExceptionWindow window = new DevelopmentExceptionWindow(sb.toString());
        App.getInstance().getAppUI().addWindow(window);
        window.focus();

        return true;
    }

}
