/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.AppContext;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaApplicationServlet extends VaadinServlet {

    private static final long serialVersionUID = -8701539520754293569L;

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        CubaVaadinServletService service = new CubaVaadinServletService(this, deploymentConfiguration);
        service.init();
        return service;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestContext.create(request, response);
        AppContext.setSecurityContext(new VaadinSessionAwareSecurityContext());
        try {
            super.service(request, response);
        } finally {
            RequestContext.destroy();
            AppContext.setSecurityContext(null);
        }
    }
}