/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class CubaApplicationContext extends WebApplicationContext {
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
     * @param application Application
     * @return CommunicationManager for Application
     */
    @SuppressWarnings("unused")
    public CommunicationManager getCommunicationManager(Application application) {
        return (CommunicationManager) applicationToAjaxAppMgrMap.get(application);
    }
}