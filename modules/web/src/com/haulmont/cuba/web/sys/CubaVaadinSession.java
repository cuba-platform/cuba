/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.web.auth.RequestContext;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.util.CurrentInstance;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaVaadinSession extends VaadinSession {

    protected transient boolean reinitializingSession = false;

    /**
     * Gets the currently used session. The current session is automatically
     * defined when processing requests to the server and in threads started at
     * a point when the current session is defined (see
     * {@link InheritableThreadLocal}). In other cases, (e.g. from background
     * threads started in some other way), the current session is not
     * automatically defined.
     *
     * @return the current session instance if available, otherwise
     *         <code>null</code>
     *
     * @see #setCurrent(VaadinSession)
     *
     * @since 7.0
     */
    public static CubaVaadinSession getCurrent() {
        return (CubaVaadinSession) CurrentInstance.get(VaadinSession.class);
    }

    /**
     * Creates a new VaadinSession tied to a VaadinService.
     *
     * @param service the Vaadin service for the new session
     */
    public CubaVaadinSession(VaadinService service) {
        super(service);
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

    public void reinitialize() {
        reinitializingSession = true;

        try {
            HttpSession oldSession = ((WrappedHttpSession)session).getHttpSession();
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

            session = new WrappedHttpSession(newSession);
        } finally {
            reinitializingSession = false;
        }
    }
}