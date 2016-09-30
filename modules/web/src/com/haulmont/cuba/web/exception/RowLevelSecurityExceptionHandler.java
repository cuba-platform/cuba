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

import com.google.common.collect.Iterables;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.RowLevelSecurityException;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Locale;

/**
 * Handles {@link com.haulmont.cuba.core.global.RowLevelSecurityException}.
 *
 */
public class RowLevelSecurityExceptionHandler extends AbstractExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RowLevelSecurityExceptionHandler.class);

    protected Window dialog;
    protected Locale locale;


    public RowLevelSecurityExceptionHandler() {
        super(RowLevelSecurityException.class.getName());

        Connection connection = App.getInstance().getConnection();
        //noinspection ConstantConditions
        locale = connection.getSession().getLocale();
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        try {
            // we may show two or more dialogs if user pressed F5 and we have no valid user session
            // just remove previous dialog and show new
            if (dialog != null) {
                app.getAppUI().removeWindow(dialog);
            }

            Messages messages = AppBeans.get(Messages.NAME);
            String userCaption = null;
            String userMessage = null;

            if (throwable != null) {
                Throwable rootCause = ExceptionUtils.getRootCause(throwable);
                RowLevelSecurityException rowLevelSecurityException = null;
                if (throwable instanceof RowLevelSecurityException) {
                    rowLevelSecurityException = (RowLevelSecurityException) throwable;
                } else if (rootCause instanceof RowLevelSecurityException) {
                    rowLevelSecurityException = (RowLevelSecurityException) rootCause;
                }

                if (rowLevelSecurityException != null) {
                    String entity = rowLevelSecurityException.getEntity();
                    String entityName = entity.split("\\$")[1];
                    MetaClass entityClass = AppBeans.get(Metadata.NAME, Metadata.class).getClassNN(entity);
                    String entityCaption = messages.getTools().getEntityCaption(entityClass, locale);

                    ConstraintOperationType operationType = rowLevelSecurityException.getOperationType();
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

            WebWindowManager wm = app.getWindowManager();
            wm.showNotification(userCaption, userMessage, Frame.NotificationType.ERROR);

            Collection<Window> windows = app.getAppUI().getWindows();
            if (!windows.isEmpty()) {
                dialog = Iterables.getLast(windows);
            }
        } catch (Throwable th) {
            log.error("Unable to handle RowLevelSecurityException", throwable);
            log.error("Exception in RowLevelSecurityExceptionHandler", th);
        }
    }
}