/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.web.App;
import com.vaadin.data.Validator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

import javax.annotation.Nullable;

/**
 */
public class InvalidValueExceptionHandler extends AbstractExceptionHandler {

    public InvalidValueExceptionHandler() {
        super(Validator.InvalidValueException.class.getName());
    }

    @Override
    public boolean handle(ErrorEvent event, App app) {
        boolean handled = super.handle(event, app);

        //noinspection ThrowableResultOfMethodCallIgnored
        if (handled && event.getThrowable() != null) {
            // Finds the original source of the error/exception
            AbstractComponent component = DefaultErrorHandler.findAbstractComponent(event);
            if (component != null) {
                component.markAsDirty();
            }

            if (component instanceof Component.Focusable) {
                ((Component.Focusable) component).focus();
            }

            //noinspection ThrowableResultOfMethodCallIgnored
            if (event.getThrowable() instanceof Validator.InvalidValueException) {
                app.getAppUI().discardAccumulatedEvents();
            }
        }
        return handled;
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        Messages messages = AppBeans.get(Messages.NAME);
        app.getWindowManager().showNotification(
                messages.getMessage(getClass(), "validationFail.caption"),
                messages.getMessage(getClass(), "validationFail"),
                Frame.NotificationType.TRAY
        );
    }
}