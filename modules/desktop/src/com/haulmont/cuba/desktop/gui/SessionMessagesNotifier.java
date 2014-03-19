/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.Connection;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(SessionMessagesNotifier.NAME)
public class SessionMessagesNotifier {

    public final static String NAME = "cuba_SessionMessagesNotifier";

    private final static Log log = LogFactory.getLog(SessionMessagesNotifier.class);

    protected Timer timer;
    protected SwingWorker<String, Void> asyncMessageLoader;

    public void activate() {
        if (timer != null) {
            return;
        }

        Configuration configuration = AppBeans.get(Configuration.NAME);
        int timeout = configuration.getConfig(DesktopConfig.class).getSessionMessagesTimeoutSec();

        if (timeout > 0) {
            timer = new Timer(timeout * 1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    syncMessages();
                }
            });
            timer.start();
        }
    }

    public void deactivate() {
        if (timer != null) {
            timer.stop();
            if (asyncMessageLoader != null) {
                asyncMessageLoader.cancel(true);
                asyncMessageLoader = null;
            }
            timer = null;
        }
    }

    protected void syncMessages() {
        Connection connection = App.getInstance().getConnection();
        if (connection.isConnected()) {
            log.trace("Check session messages");

            asyncMessageLoader = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    try {
                        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
                        return uss.getMessages();
                    } catch (NoUserSessionException e) {
                        log.warn("Unable to get messages for session, user session not found");
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        processServerMessage(get());
                    } catch (InterruptedException | ExecutionException ignored) {
                        // do nothing
                    } finally {
                        asyncMessageLoader = null;
                    }
                }
            };
            asyncMessageLoader.execute();
        }
    }

    protected void processServerMessage(String message) {
        if (message != null) {
            log.debug("Received session message");

            Connection connection = App.getInstance().getConnection();
            if (connection.isConnected() && timer != null && timer.isRunning()) {
                SessionMessageWindow dialog = new SessionMessageWindow(App.getInstance().getMainFrame());
                dialog.setMessage(message);
                dialog.setVisible(true);
            }
        }
    }
}