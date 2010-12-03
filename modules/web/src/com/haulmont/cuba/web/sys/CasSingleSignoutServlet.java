/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 24.11.2010 11:52:37
 *
 * $Id: CasSingleSignoutServlet.java 3252 2010-11-25 12:30:12Z gorodnov $
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
