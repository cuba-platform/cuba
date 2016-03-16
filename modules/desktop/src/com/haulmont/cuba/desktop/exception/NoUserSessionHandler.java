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

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.global.NoUserSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Handles {@link NoUserSessionException}.
 *
 */
public class NoUserSessionHandler extends AbstractExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(NoUserSessionHandler.class);

    private boolean fired;

    public NoUserSessionHandler() {
        super(NoUserSessionException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        if (fired) // This handler should fire only once in a session
            return;

        try {
            App.getInstance().getMainFrame().getWindowManager().showOptionDialog(
                    getMessage("dialogs.Information"),
                    getMessage("noUserSession.message"),
                    Frame.MessageType.CONFIRMATION,
                    new Action[] {
                            new LoginAction(),
                            new ExitAction()
                    }
            );
            fired = true;
        } catch (Throwable th) {
            log.error("Error handling exception", th);
        }
    }

    protected String getMessage(String key) {
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMainMessage(key, App.getInstance().getLocale());
    }

    private class LoginAction extends DialogAction {
        protected LoginAction() {
            super(DialogAction.Type.OK, Status.PRIMARY);
        }

        @Override
        public void actionPerform(Component component) {
            App app = App.getInstance();
            app.getConnection().logout();
        }
    }

    private class ExitAction extends AbstractAction {
        protected ExitAction() {
            super("exit");
        }

        @Override
        public void actionPerform(Component component) {
            System.exit(0);
        }

        @Override
        public String getCaption() {
            return getMessage("noUserSession.exit");
        }
    }
}
