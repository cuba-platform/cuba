/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.web.auth.RequestContext;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Enumeration;
import java.util.HashMap;

@SuppressWarnings("serial")
public class CubaApplicationContext extends WebApplicationContext {

    private transient boolean reinitializingSession = false;

    protected CubaApplicationContext() {
    }

    static public CubaApplicationContext getExistingApplicationContext(HttpSession session) {
        return (CubaApplicationContext) session
                .getAttribute(WebApplicationContext.class.getName());
    }

    static public CubaApplicationContext getApplicationContext(HttpSession session) {
        CubaApplicationContext cx = (CubaApplicationContext) session
                .getAttribute(WebApplicationContext.class.getName());
        if (cx == null) {
            cx = new CubaApplicationContext();
            session.setAttribute(WebApplicationContext.class.getName(), cx);
        }
        if (cx.session == null) {
            cx.session = session;
        }
        return cx;
    }

    public void reinitializeSession() {
        reinitializingSession = true;

        try {
            HttpSession oldSession = getHttpSession();
            HashMap<String, Object> attrs = new HashMap<>();

            for (Enumeration<String> e = oldSession.getAttributeNames(); e
                    .hasMoreElements(); ) {
                String name = e.nextElement();
                attrs.put(name, oldSession.getAttribute(name));
            }

            // Invalidate the current session
            oldSession.invalidate();

            HttpSession newSession = RequestContext.get().getSession();

            for (String name : attrs.keySet()) {
                newSession.setAttribute(name, attrs.get(name));
            }

            session = newSession;
        } finally {
            reinitializingSession = false;
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        if (!reinitializingSession) {
            // Avoid closing the application if we are only reinitializing the
            // session. Closing the application would cause the state to be lost
            // and a new application to be created, which is not what we want.
            super.valueUnbound(event);
        }
    }

    @Override
    protected CommunicationManager createCommunicationManager(Application application,
                                                              AbstractApplicationServlet applicationServlet) {
        return new CubaCommunicationManager(application);
    }

    @Override
    protected void startTransaction(Application application, Object request) {
        super.startTransaction(application, request);
    }

    @Override
    protected void endTransaction(Application application, Object request) {
        super.endTransaction(application, request);
    }

    /**
     * Uses in HTTP controllers for access to Vaadin components
     *
     * @param application Application
     * @return CommunicationManager for Application
     */
    @SuppressWarnings("unused")
    public CommunicationManager getCommunicationManager(Application application) {
        return (CommunicationManager) applicationToAjaxAppMgrMap.get(application);
    }
}