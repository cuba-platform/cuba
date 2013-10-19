/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.actions;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Window;

/**
 * <p>$Id$</p>
 *
 * @author shishov
 */

public class ChangeSubstUserAction extends AbstractAction {
    private User user;

    public ChangeSubstUserAction(User user) {
        super("changeSubstUserAction");
        this.user = user;
    }

    @Override
    public String getIcon() {
        return "icons/ok.png";
    }

    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        final App app = App.getInstance();
        app.getWindowManager().checkModificationsAndCloseAll(
                new Runnable() {
                    public void run() {
                        app.getWindowManager().closeAll();
                        try {
                            app.getConnection().substituteUser(user);
                            doAfterChangeUser();
                        } catch (javax.persistence.NoResultException e) {
                            App.getInstance().getAppWindow().showNotification(
                                    MessageProvider.formatMessage(AppConfig.getMessagesPack(), "userDeleteMsg",
                                    user.getName()),
                                    Window.Notification.TYPE_WARNING_MESSAGE
                            );
                            doRevert();
                        }
                    }
                },
                new Runnable() {
                    public void run() {
                        doRevert();
                    }
                }
        );
    }

    public void doAfterChangeUser() {}

    public void doRevert() {}
}
