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

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.core.global.EntityAccessException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Handles {@link EntityAccessException}.
 *
 */
@Component("cuba_EntityAccessExceptionHandler")
public class EntityAccessExceptionHandler extends AbstractGenericExceptionHandler {

    @Inject
    protected Messages messages;

    public EntityAccessExceptionHandler() {
        super(EntityAccessException.class.getName());
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        String msg = messages.formatMessage(getClass(), "entityAccessException.message");
        windowManager.showNotification(msg, Frame.NotificationType.WARNING);
    }
}