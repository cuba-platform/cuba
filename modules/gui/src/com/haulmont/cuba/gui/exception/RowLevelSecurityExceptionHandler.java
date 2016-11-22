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
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.RowLevelSecurityException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component("cuba_RowLevelSecurityExceptionHandler")
public class RowLevelSecurityExceptionHandler extends AbstractGenericExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(RowLevelSecurityExceptionHandler.class);

    protected RowLevelSecurityExceptionHandler() {
        super("com.haulmont.cuba.core.global.RowLevelSecurityException");
    }

    @Override
    protected boolean canHandle(String className, String message, @Nullable Throwable throwable) {
        return className.equals("com.haulmont.cuba.core.global.RowLevelSecurityException");
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        try {
            Messages messages = AppBeans.get(Messages.NAME);
            String userCaption = null;
            String userMessage = null;

            if (throwable != null) {
                //noinspection ThrowableResultOfMethodCallIgnored
                Throwable rootCause = ExceptionUtils.getRootCause(throwable);
                RowLevelSecurityException exception = null;
                if (throwable instanceof RowLevelSecurityException) {
                    exception = (RowLevelSecurityException) throwable;
                } else if (rootCause instanceof RowLevelSecurityException) {
                    exception = (RowLevelSecurityException) rootCause;
                }

                if (exception != null) {
                    String entity = exception.getEntity();
                    String entityName = entity.split("\\$")[1];
                    MetaClass entityClass = AppBeans.get(Metadata.NAME, Metadata.class).getClassNN(entity);
                    String entityCaption = messages.getTools().getEntityCaption(entityClass);

                    ConstraintOperationType operationType = exception.getOperationType();
                    if (operationType != null) {
                        String operationId = operationType.getId();
                        String customCaptionKey = String.format("rowLevelSecurity.caption.%s.%s", entityName, operationId);
                        String customCaption = messages.getMainMessage(customCaptionKey);
                        if (!customCaptionKey.equals(customCaption)) {
                            userCaption = customCaption;
                        }

                        String customMessageKey = String.format("rowLevelSecurity.entityAndOperationMessage.%s.%s",
                                entityName, operationId);
                        String customMessage = messages.getMainMessage(customMessageKey);
                        if (!customMessageKey.equals(customMessage)) {
                            userMessage = customMessage;
                        } else {
                            userMessage = messages.formatMainMessage("rowLevelSecurity.entityAndOperationMessage",
                                    messages.getMessage(operationType), entityCaption);
                        }
                    } else {
                        String customCaptionKey = String.format("rowLevelSecurity.caption.%s", entityName);
                        String customCaption = messages.getMainMessage(customCaptionKey);
                        if (!customCaptionKey.equals(customCaption)) {
                            userCaption = customCaption;
                        }

                        String customMessageKey = String.format("rowLevelSecurity.entityMessage.%s", entityName);
                        String customMessage = messages.getMainMessage(customMessageKey);
                        if (!customMessageKey.equals(customMessage)) {
                            userMessage = customMessage;
                        } else {
                            userMessage = messages.formatMainMessage("rowLevelSecurity.entityMessage", entityCaption);
                        }
                    }
                }
            }

            if (StringUtils.isEmpty(userCaption)) {
                userCaption = messages.getMainMessage("rowLevelSecurity.caption");
            }
            windowManager.showNotification(userCaption, userMessage, Frame.NotificationType.ERROR);
        } catch (Throwable th) {
            log.error("Unable to handle RowLevelSecurityException", throwable);
            log.error("Exception in RowLevelSecurityExceptionHandler", th);
        }
    }
}