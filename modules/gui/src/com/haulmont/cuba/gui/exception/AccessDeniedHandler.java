/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_AccessDeniedHandler")
public class AccessDeniedHandler extends AbstractGenericExceptionHandler implements Ordered {

    @Inject
    protected Messages messages;

    public AccessDeniedHandler() {
        super(AccessDeniedException.class.getName());
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        String msg = messages.getMessage(getClass(), "accessDenied.message");
        windowManager.showNotification(msg, Frame.NotificationType.ERROR);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 20;
    }
}
