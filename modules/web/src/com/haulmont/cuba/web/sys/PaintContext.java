/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys;

/**
 * @author artamonov
 * @version $Id$
 */
public class PaintContext {

    private static boolean painting = false;

    static void paintStarted() {
        painting = true;
    }

    static void paintFinished() {
        painting = false;
    }

    public static boolean isPainting() {
        if (RequestContext.get() == null)
            throw new IllegalStateException("Could not check painting status without client request");

        return painting;
    }
}