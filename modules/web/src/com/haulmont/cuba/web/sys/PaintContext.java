/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys;

import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.BooleanUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public class PaintContext {

    private static final String PAINTING_SESSION_ATTRIBUTE = "painting";

    static void paintStarted() {
        if (VaadinSession.getCurrent() == null)
            throw new IllegalStateException("Could not modify painting status without vaadin session");

        VaadinSession.getCurrent().setAttribute(PAINTING_SESSION_ATTRIBUTE, true);
    }

    static void paintFinished() {
        if (VaadinSession.getCurrent() == null)
            throw new IllegalStateException("Could not modify painting status without vaadin session");

        VaadinSession.getCurrent().setAttribute(PAINTING_SESSION_ATTRIBUTE, false);
    }

    public static boolean isPainting() {
        if (RequestContext.get() == null)
            throw new IllegalStateException("Could not check painting status without client request");

        Boolean paintingValue = (Boolean) VaadinSession.getCurrent().getAttribute(PAINTING_SESSION_ATTRIBUTE);
        return paintingValue != null && BooleanUtils.isTrue(paintingValue);
    }
}