/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.exception;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.DeletePolicyException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles {@link DeletePolicyException}. Determines the exception type by searching a special marker string in the
 * messages of all exceptions in the chain.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_DeletePolicyHandler")
public class DeletePolicyHandler implements GenericExceptionHandler, Ordered {

    @Inject
    protected Messages messages;

    @Inject
    protected Metadata metadata;

    @Override
    public boolean handle(Throwable exception, WindowManager windowManager) {
        Throwable t = exception;
        try {
            while (t != null) {
                if (t.toString().contains(getMarker())) {
                    doHandle(t.toString(), windowManager);
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

    protected void doHandle(String message, WindowManager windowManager) {
        String msg = messages.getMessage(getClass(), "deletePolicy.message");

        MetaClass metaClass = recognizeMetaClass(message);
        if (metaClass != null) {
            String localizedEntityName = messages.getTools().getEntityCaption(metaClass);
            String references = messages.getMessage(getClass(), "deletePolicy.references.message");
            msg += "\n" + references + " \"" + localizedEntityName + "\"";
        }

        windowManager.showNotification(msg, Frame.NotificationType.ERROR);
    }

    @Nullable
    protected MetaClass recognizeMetaClass(String message) {
        Matcher matcher = getPattern().matcher(message);
        if (matcher.find()) {
            String entityName = matcher.group(2);
            if (!StringUtils.isEmpty(entityName)) {
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

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 30;
    }
}