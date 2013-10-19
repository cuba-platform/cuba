/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.executors.impl;

import com.haulmont.cuba.web.App;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class BackgroundWorkerListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        // Do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

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