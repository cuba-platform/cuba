/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles {@link DeletePolicyException}. Determines the exception type by searching a special marker string in the
 * messages of all exceptions in the chain.
 *
 * <p>$Id$</p>
 *
 * @author devyatkin
 * @version $Id$
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
        return DeletePolicyException.class.getName();
    }

    protected Pattern getPattern() {
        return Pattern.compile(
                DeletePolicyException.class.getName() + ": " + DeletePolicyException.ERR_MESSAGE.replace("%s", "(\\w+\\$\\w+)"));
    }

    protected void doHandle(Thread thread, String message) {
        Messages messages = AppBeans.get(Messages.NAME);

        String msg = messages.getMessage(getClass(), "deletePolicy.message");

        MetaClass metaClass = recognizeMetaClass(message);
        if (metaClass != null) {
            String localizedEntityName = messages.getTools().getEntityCaption(metaClass);
            String references = messages.getMessage(getClass(), "deletePolicy.references.message");
            App.getInstance().getMainFrame().showNotification(msg, references + " \"" + localizedEntityName + "\"",
                    IFrame.NotificationType.ERROR);
        } else {
            App.getInstance().getMainFrame().showNotification(msg, IFrame.NotificationType.ERROR);
        }
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