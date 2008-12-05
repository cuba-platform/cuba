/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.12.2008 14:37:46
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.UserSession;
import com.itmill.toolkit.Application;
import com.itmill.toolkit.service.ApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

import javax.servlet.http.HttpServletRequest;

public class App extends Application
{
    private Log log = LogFactory.getLog(App.class);

    private Connection connection;

    static {
        SecurityAssociation.setServer();
    }

    public App() {
        this.connection = new Connection();
    }

    public void init() {
        log.debug("Initializing application");

        AppWindow appWindow = getAppWindow();
        connection.addListener(appWindow);

        ApplicationContext appContext = getContext();
        appContext.addTransactionListener(new RequestListener());
    }

    protected AppWindow getAppWindow() {
        return new AppWindow(this);
    }

    public Connection getConnection() {
        return connection;
    }

    private class RequestListener implements ApplicationContext.TransactionListener
    {
        public void transactionStart(Application application, Object transactionData) {
            if (log.isTraceEnabled()) {
                HttpServletRequest request = (HttpServletRequest) transactionData;
                log.trace("requestStart: " + request + " from " + request.getRemoteAddr());
            }
            UserSession userSession = connection.getSession();
            if (userSession != null) {
                SecurityAssociation.setPrincipal(new SimplePrincipal(userSession.getLogin()));
                SecurityAssociation.setCredential(userSession.getId().toString().toCharArray());
            }
        }

        public void transactionEnd(Application application, Object transactionData) {
            if (log.isTraceEnabled()) {
                log.trace("requestEnd: " + transactionData);
            }
        }
    }
}
