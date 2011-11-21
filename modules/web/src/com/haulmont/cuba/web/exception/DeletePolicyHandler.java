/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.web.App;
import com.vaadin.terminal.Terminal;
import com.vaadin.ui.Window;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles {@link DeletePolicyException}. Determines the exception type by searching a special marker string in the
 * messages of all exceptions in the chain.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DeletePolicyHandler implements ExceptionHandler {

    @Override
    public boolean handle(Terminal.ErrorEvent event, App app) {
        Throwable t = event.getThrowable();
        try {
            while (t != null) {
                if (t.toString().contains(getMarker())) {
                    doHandle(t.toString(), app);
                    return true;
                }
                t = t.getCause();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected String getMarker() {
        return DeletePolicyException.ERR_MESSAGE;
    }

    protected void doHandle(String message, App app) {
        String localizedEntityName;
        MetaClass metaClass = recognizeMetaClass(message);
        if (metaClass != null) {
            String entityName = metaClass.getName();
            localizedEntityName = MessageProvider.getMessage(metaClass.getJavaClass(),
                    entityName.substring(entityName.lastIndexOf("$") + 1));
        } else {
            localizedEntityName = "";
        }
        String msg = MessageProvider.getMessage(getClass(), "deletePolicy.message");
        String references = MessageProvider.getMessage(getClass(), "deletePolicy.references.message");
        app.getAppWindow().showNotification(msg + "<br>" + references + " \"" + localizedEntityName + "\"",
                Window.Notification.TYPE_ERROR_MESSAGE);
    }

    protected MetaClass recognizeMetaClass(String message) {
        Matcher matcher = Pattern.compile(getMarker() + "(.*)")
                .matcher(message);
        if (matcher.find()) {
            String entityName = matcher.group(1);
            return MetadataProvider.getSession().getClass(entityName);
        } else {
            return null;
        }
    }
}
