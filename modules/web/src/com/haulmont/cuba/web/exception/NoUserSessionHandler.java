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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Locale;

/**
 * Handles {@link NoUserSessionException}.
 *
 */
public class NoUserSessionHandler extends AbstractExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(NoUserSessionHandler.class);

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
                    messages.getMainMessage("dialogs.Information", locale),
                    messages.getMainMessage("noUserSession.message", locale),
                    Frame.MessageType.CONFIRMATION,
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