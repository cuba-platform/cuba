/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.web.App;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collection;

/**
 * {@link AppContext} loader of the web client application.
 *
 * @author krivopustov
 * @version $Id$
 */
public class WebAppContextLoader extends AbstractWebAppContextLoader implements HttpSessionListener {

    @Override
    protected void beforeInitAppContext() {
        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        // Do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // stop background tasks
        HttpSession session = httpSessionEvent.getSession();

        WebApplicationContext applicationContext = WebApplicationContext.getApplicationContext(session);

        final Collection<Application> applications = applicationContext.getApplications();

        // Cleanup tasks in applications
        for (Application app : applications) {
            // Purge threads
            ((App)app).cleanupBackgroundTasks();
        }
    }
}