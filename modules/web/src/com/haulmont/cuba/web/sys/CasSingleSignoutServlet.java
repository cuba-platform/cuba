/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CasSingleSignoutServlet extends HttpServlet {

    private String casLogoutUrl;
    
    private static final long serialVersionUID = 1033938992027376182L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        casLogoutUrl = config.getInitParameter("casLogoutUrl");
        if (casLogoutUrl == null) {
            throw new IllegalStateException("Parameter 'casLogoutUrl' cannot be NULL");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getSession().invalidate();
        resp.sendRedirect(casLogoutUrl);
    }
}
