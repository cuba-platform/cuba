/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DeletePolicyException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.App;
import com.vaadin.server.ErrorEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles {@link DeletePolicyException}. Determines the exception type by searching a special marker string in the
 * messages of all exceptions in the chain.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DeletePolicyHandler implements ExceptionHandler {

    @Override
    public boolean handle(ErrorEvent event, App app) {
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
        Messages messages = AppBeans.get(Messages.NAME);

        String localizedEntityName;
        MetaClass metaClass = recognizeMetaClass(message);
        if (metaClass != null) {
            String entityName = metaClass.getName();
            localizedEntityName = messages.getMessage(metaClass.getJavaClass(),
                    entityName.substring(entityName.lastIndexOf("$") + 1));
        } else {
            localizedEntityName = "";
        }
        String msg = messages.getMessage(getClass(), "deletePolicy.message");
        String references = messages.getMessage(getClass(), "deletePolicy.references.message");

        msg += "\n" + references + " \"" + localizedEntityName + "\"";
        app.getWindowManager().showNotification(msg, IFrame.NotificationType.ERROR);
    }

    protected MetaClass recognizeMetaClass(String message) {
        Matcher matcher = Pattern.compile(getMarker() + "(.*)")
                .matcher(message);
        if (matcher.find()) {
            String entityName = matcher.group(1);
            Metadata metadata = AppBeans.get(Metadata.NAME);
            return metadata.getClass(entityName);
        } else {
            return null;
        }
    }
}