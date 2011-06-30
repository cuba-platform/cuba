/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.09.2009 15:07:23
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.web.App;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.vaadin.terminal.ExternalResource;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.Locale;

public class NoUserSessionHandler extends AbstractExceptionHandler<NoUserSessionException> {

    private static Log log = LogFactory.getLog(NoUserSessionHandler.class);
    private Locale locale;

    public NoUserSessionHandler() {
        super(NoUserSessionException.class);
        locale = App.getInstance().getConnection().getSession().getLocale();
    }

    protected void doHandle(NoUserSessionException t, App app) {
        try {
            App.getInstance().getWindowManager().showOptionDialog(
                MessageProvider.getMessage(getClass(), "dialogs.Information", locale),
                    MessageProvider.getMessage(getClass(), "noUserSession.message", locale),
                    IFrame.MessageType.CONFIRMATION,
                    new Action[] {new LoginAction()}
            );
        } catch (Throwable th) {
            log.error(th);
        }
    }

    private class LoginAction extends AbstractAction {
        protected LoginAction() {
            super("actions.Ok");
        }

        public void actionPerform(Component component) {
            App app = App.getInstance();
            String restartUrl = app.getURL().toString() + "?restartApp";
            app.getAppWindow().open(new ExternalResource(restartUrl));
        }

        @Override
        public String getCaption() {
            return MessageProvider.getMessage(getClass(), "actions.Ok", locale);
        }

        @Override
        public String getIcon() {
            return "icons/ok.png";
        }
    }
}
