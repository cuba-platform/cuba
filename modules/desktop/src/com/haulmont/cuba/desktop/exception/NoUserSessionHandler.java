/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.global.NoUserSessionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class NoUserSessionHandler extends AbstractExceptionHandler<NoUserSessionException> {

    private static Log log = LogFactory.getLog(NoUserSessionHandler.class);

    public NoUserSessionHandler() {
        super(NoUserSessionException.class);
    }

    @Override
    protected void doHandle(Thread thread, NoUserSessionException e) {
        try {
            App.getInstance().getWindowManager().showOptionDialog(
                    getMessage("dialogs.Information"),
                    getMessage("noUserSession.message"),
                    IFrame.MessageType.CONFIRMATION,
                    new Action[] {
                            new LoginAction(),
                            new ExitAction()
                    }
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
            app.getConnection().logout();
        }

        @Override
        public String getCaption() {
            return getMessage("actions.Ok");
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
