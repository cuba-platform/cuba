/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.log.DevelopmentExceptionWindow;
import com.vaadin.server.ErrorEvent;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hasanov
 * @version $Id$
 */
public class DevelopmentExceptionHandler extends AbstractExceptionHandler {

    public DevelopmentExceptionHandler() {
        super(DevelopmentException.class.getName());
    }

    @Override
    public boolean handle(ErrorEvent event, App app) {
        return AppBeans.get(Configuration.class).getConfig(ClientConfig.class).getDevelopmentExceptionEnabled()
                && super.handle(event, app);
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {

        if (throwable == null)
            return;
        Messages messages = AppBeans.get(Messages.class);
        Map<String, Object> tableMap = new HashMap<>(8);
        Map<String,Object> info;
        String frameId;
        StringBuilder rootCauseMessage = new StringBuilder(messages.getMessage(getClass(),"exceptionDialog.message"));
        StringBuilder stackTrace = new StringBuilder();
        info = ((DevelopmentException) throwable).info;
        frameId = ((DevelopmentException) throwable).frameId;
        rootCauseMessage.append(throwable.getMessage());
        stackTrace.append("<br/>");
        stackTrace.append(StringUtils.replace(
                StringEscapeUtils.escapeHtml(ExceptionUtils.getStackTrace(throwable)), "\n", "<br/>"));
        stackTrace.append("<br/>");
        final WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
        if (frameId != null){
            tableMap.put("Screen Descriptor", windowConfig.getWindowInfo(frameId).getTemplate());
            tableMap.put("Frame Id", frameId);
        }
        if (info != null)
            tableMap.putAll(info);

        DevelopmentExceptionWindow devWindow = new DevelopmentExceptionWindow(rootCauseMessage.toString(),
                stackTrace.toString(), tableMap);
        for (Window window : App.getInstance().getAppUI().getWindows()){
            if (window.isModal()){
                devWindow.setModal(true);
                break;
            }
        }
        App.getInstance().getAppUI().addWindow(devWindow);
        devWindow.focus();

    }

}
