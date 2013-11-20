/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.actions;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;

/**
 * @author shishov
 * @version $Id$
 */
public class ChangeSubstUserAction extends AbstractAction {
    protected User user;

    public ChangeSubstUserAction(User user) {
        super("changeSubstUserAction");
        this.user = user;
    }

    @Override
    public String getIcon() {
        return "icons/ok.png";
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        final App app = App.getInstance();
        app.getWindowManager().checkModificationsAndCloseAll(
                new Runnable() {
                    @Override
                    public void run() {
                        app.closeAllWindows();
                        try {
                            app.getConnection().substituteUser(user);
                            doAfterChangeUser();
                        } catch (javax.persistence.NoResultException e) {
                            app.getWindowManager().showNotification(
                                    messages.formatMessage(AppConfig.getMessagesPack(), "substitutionNotPerformed",
                                            user.getName()),
                                    IFrame.NotificationType.WARNING
                            );
                            doRevert();
                        }
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        doRevert();
                    }
                }
        );
    }

    public void doAfterChangeUser() {
    }

    public void doRevert() {
    }
}