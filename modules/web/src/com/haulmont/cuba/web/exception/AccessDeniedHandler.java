/*
 * Copyright (c) 2008-2020 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.BeanLocatorAware;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;

import javax.annotation.Nullable;

/**
 * Handles {@link AccessDeniedException}
 */
public class AccessDeniedHandler extends AbstractExceptionHandler implements BeanLocatorAware {

    protected BeanLocator beanLocator;

    public AccessDeniedHandler() {
        super(AccessDeniedException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        Messages messages = beanLocator.get(Messages.class);
        WebConfig config = beanLocator.get(Configuration.class).getConfig(WebConfig.class);

        String msg;
        if (throwable != null && !config.getProductionMode()) {
            AccessDeniedException e = (AccessDeniedException) throwable;
            msg = messages.formatMessage(getClass(), "accessDenied.detailedMessage", e.getTarget(),
                    messages.getMessage(e.getType()) + (e.getEntityOp() != null ? " (" + messages.getMessage(e.getEntityOp()) + ")" : ""));
        } else {
            msg = messages.getMessage(getClass(), "accessDenied.message");
        }

        AppUI.getCurrent().getNotifications()
                .create(Notifications.NotificationType.ERROR)
                .withCaption(msg)
                .show();
    }

    @Override
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

}
