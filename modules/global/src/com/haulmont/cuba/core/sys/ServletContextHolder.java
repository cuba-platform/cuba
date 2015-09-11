/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import javax.servlet.ServletContext;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class ServletContextHolder {
    private static volatile ServletContext servletContext;

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static void setServletContext(ServletContext servletContext) {
        ServletContextHolder.servletContext = servletContext;
    }
}
