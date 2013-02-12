/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaVaadinServletService extends VaadinServletService {

    private WebConfig webConfig;

    public CubaVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) {
        super(servlet, deploymentConfiguration);

        webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);

        // vaadin7 supply localized system messages
/*        setSystemMessagesProvider(new SystemMessagesProvider() {
            @Override
            public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
                return null;
            }
        });*/
    }

    @Override
    public String getConfiguredTheme(VaadinRequest request) {
        // vaadin7 return theme from user settings or use system default
        return webConfig.getAppWindowTheme();
    }
}