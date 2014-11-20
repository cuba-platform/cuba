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
import com.vaadin.terminal.Terminal;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
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
        return DeletePolicyException.class.getName();
    }

    protected Pattern getPattern() {
        return Pattern.compile(
                DeletePolicyException.class.getName() + ": " + DeletePolicyException.ERR_MESSAGE.replace("%s", "(\\w+\\$\\w+)"));
    }

    protected void doHandle(String message, App app) {
        Messages messages = AppBeans.get(Messages.NAME);

        String msg = messages.getMessage(getClass(), "deletePolicy.message");

        MetaClass metaClass = recognizeMetaClass(message);
        if (metaClass != null) {
            String localizedEntityName = messages.getTools().getEntityCaption(metaClass);
            String references = messages.getMessage(getClass(), "deletePolicy.references.message");
            msg += "\n" + references + " \"" + localizedEntityName + "\"";
        }

        app.getWindowManager().showNotification(msg, IFrame.NotificationType.ERROR);
    }

    @Nullable
    protected MetaClass recognizeMetaClass(String message) {
        Matcher matcher = getPattern().matcher(message);
        if (matcher.find()) {
            String entityName = matcher.group(2);
            if (!StringUtils.isEmpty(entityName)) {
                Metadata metadata = AppBeans.get(Metadata.NAME);
                MetaClass metaClass = metadata.getClass(entityName);
                if (metaClass != null) {
                    MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                    metaClass = originalMetaClass != null ? originalMetaClass : metaClass;
                }
                return metaClass;
            }
        }
        return null;
    }
}