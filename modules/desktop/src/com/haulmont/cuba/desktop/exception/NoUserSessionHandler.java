/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.global.NoUserSessionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;

/**
 * Handles {@link NoUserSessionException}.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class NoUserSessionHandler extends AbstractExceptionHandler {

    private static Log log = LogFactory.getLog(NoUserSessionHandler.class);

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
                    IFrame.MessageType.CONFIRMATION,
                    new Action[] {
                            new LoginAction(),
                            new ExitAction()
                    }
            );
            fired = true;
        } catch (Throwable th) {
            log.error(th);
        }
    }

    private class LoginAction extends DialogAction {
        protected LoginAction() {
            super(DialogAction.Type.OK);
        }

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
