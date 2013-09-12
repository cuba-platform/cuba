/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.vaadin.server.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Handles {@link NoUserSessionException}.
 *
 * @author krivopustov
 * @version $Id$
 */
public class NoUserSessionHandler extends AbstractExceptionHandler {

    private static Log log = LogFactory.getLog(NoUserSessionHandler.class);
    private Locale locale;

    public NoUserSessionHandler() {
        super(NoUserSessionException.class.getName());
        //noinspection ConstantConditions
        locale = App.getInstance().getConnection().getSession().getLocale();
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        try {
            Messages messages = AppBeans.get(Messages.class);

            App.getInstance().getWindowManager().showOptionDialog(
                    messages.getMessage(getClass(), "dialogs.Information", locale),
                    messages.getMessage(getClass(), "noUserSession.message", locale),
                    IFrame.MessageType.CONFIRMATION,
                    new Action[] {new LoginAction()}
            );
        } catch (Throwable th) {
            log.error(th);
        }
    }

    private class LoginAction extends DialogAction {
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