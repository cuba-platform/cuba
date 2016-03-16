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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.exceptions.EclipseLinkException;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;

/**
 * Handles database "numeric overflow" exception.
 *
 */
@Component("cuba_NumericOverflowExceptionHandler")
public class NumericOverflowExceptionHandler extends AbstractGenericExceptionHandler {

    public NumericOverflowExceptionHandler() {
        // todo EL
        super(EclipseLinkException.class.getName());
    }

    @Override
    protected boolean canHandle(String className, String message, @Nullable Throwable throwable) {
        return StringUtils.containsIgnoreCase(message, "Numeric field overflow");
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        Messages messages = AppBeans.get(Messages.NAME);
        String msg = messages.getMessage(getClass(), "numericFieldOverflow.message");
        windowManager.showNotification(msg, Frame.NotificationType.ERROR);
    }
}