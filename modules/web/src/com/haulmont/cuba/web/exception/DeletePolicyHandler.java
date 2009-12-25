/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Ilya Grachev
 * Created: 29.07.2009 18:51:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeletePolicyHandler extends AbstractExceptionHandler<DeletePolicyException> {
    public DeletePolicyHandler() {
        super(DeletePolicyException.class);
    }

    protected void doHandle(DeletePolicyException t, App app) {
        Matcher matcher = Pattern.compile("there are references from (.*)")
                .matcher(t.getMessage());
        String localizedEntityName = "";
        if (matcher.find()) {
            String entityName = matcher.group(1);
            MetaClass metaClass = MetadataProvider.getSession().getClass(entityName);
            localizedEntityName = MessageProvider.getMessage(metaClass.getJavaClass(),
                    entityName.substring(entityName.lastIndexOf("$") + 1));
        }
        String msg = MessageProvider.getMessage(getClass(), "deletePolicy.message");
        String references = MessageProvider.getMessage(getClass(), "deletePolicy.references.message");
        app.getAppWindow().showNotification(msg + "<br>" + references + " \"" + localizedEntityName + "\"",
                Window.Notification.TYPE_ERROR_MESSAGE);
    }
}
