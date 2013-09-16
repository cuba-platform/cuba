/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.DeletePolicyException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles {@link DeletePolicyException}. Determines the exception type by searching a special marker string in the
 * messages of all exceptions in the chain.
 *
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class DeletePolicyHandler implements ExceptionHandler {

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        Throwable t = exception;
        try {
            while (t != null) {
                if (t.toString().contains(getMarker())) {
                    doHandle(thread, t.toString());
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

    protected void doHandle(Thread thread, String message) {
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
        App.getInstance().getMainFrame().showNotification(msg, references + " \"" + localizedEntityName + "\"",
                IFrame.NotificationType.ERROR);
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