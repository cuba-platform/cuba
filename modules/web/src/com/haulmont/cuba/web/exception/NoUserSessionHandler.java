/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Locale;

/**
 * Handles {@link NoUserSessionException}.
 *
 * @author krivopustov
 * @version $Id$
 */
public class NoUserSessionHandler extends AbstractExceptionHandler {

    private static final Log log = LogFactory.getLog(NoUserSessionHandler.class);

    private Locale locale;

    private Window noUserSessionDialog;

    public NoUserSessionHandler() {
        super(NoUserSessionException.class.getName());

        Connection connection = App.getInstance().getConnection();
        //noinspection ConstantConditions
        locale = connection.getSession().getLocale();
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        try {
            // we may show two or more dialogs if user pressed F5 and we have no valid user session
            // just remove previous dialog and show new
            if (noUserSessionDialog != null) {
                app.getAppUI().removeWindow(noUserSessionDialog);
            }

            Messages messages = AppBeans.get(Messages.NAME);

            WebWindowManager wm = app.getWindowManager();
            wm.showOptionDialog(
                    messages.getMessage(getClass(), "dialogs.Information", locale),
                    messages.getMessage(getClass(), "noUserSession.message", locale),
                    IFrame.MessageType.CONFIRMATION,
                    new Action[]{new LoginAction()}
            );

            Collection<Window> windows = app.getAppUI().getWindows();
            if (!windows.isEmpty()) {
                noUserSessionDialog = Iterables.getLast(windows);
            }
        } catch (Throwable th) {
            log.error("Unable to handle NoUserSessionException", throwable);
            log.error("Exception in NoUserSessionHandler", th);
        }
    }

    private static class LoginAction extends DialogAction {
        protected LoginAction() {
            super(DialogAction.Type.OK);
        }

        @Override
        public void actionPerform(Component component) {
            String url = ControllerUtils.getLocationWithoutParams() + "?restartApp";

            Page.getCurrent().open(url, "_self");
        }
    }
}