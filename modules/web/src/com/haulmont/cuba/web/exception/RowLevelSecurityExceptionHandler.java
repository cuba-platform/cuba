/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Locale;

/**
 * Handles {@link com.haulmont.cuba.core.global.RowLevelSecurityException}.
 *
 * @author krivopustov
 * @version $Id$
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
            String userCaption = messages.getMessage("com.haulmont.cuba.gui",
                    "rowLevelSecurity.caption");
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
                    MetaClass entityClass = AppBeans.get(Metadata.NAME, Metadata.class).getClassNN(entity);
                    String entityCaption = messages.getTools().getEntityCaption(entityClass, locale);

                    ConstraintOperationType operationType = rowLevelSecurityException.getOperationType();
                    if (operationType != null) {
                        userMessage = messages.formatMessage("com.haulmont.cuba.gui",
                                "rowLevelSecurity.entityAndOperationMessage", locale,
                                messages.getMessage(operationType), entityCaption);
                    } else if (entity != null) {
                        userMessage = messages.formatMessage("com.haulmont.cuba.gui",
                                "rowLevelSecurity.entityMessage", locale, entityCaption);
                    }
                }
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