/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

/**
 * @author gorodnov
 * @version $Id$
 */
public class CubaDispatcherServlet extends DispatcherServlet {

    private static final long serialVersionUID = -4884517938479910144L;

    public static final String SPRING_CONTEXT_CONFIG = "cuba.dispatcherSpringContextConfig";

    @Override
    public String getContextConfigLocation() {
        String configProperty = AppContext.getProperty(SPRING_CONTEXT_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + SPRING_CONTEXT_CONFIG + " application property");
        }
        File baseDir = new File(AppContext.getProperty("cuba.confDir"));

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        String[] tokenArray = tokenizer.getTokenArray();
        StringBuilder locations = new StringBuilder();
        for (String token : tokenArray) {
            String location;
            if (ResourceUtils.isUrl(token)) {
                location = token;
            } else {
                if (token.startsWith("/"))
                    token = token.substring(1);
                File file = new File(baseDir, token);
                if (file.exists()) {
                    location = file.toURI().toString();
                } else {
                    location = "classpath:" + token;
                }
            }
            locations.append(location).append(" ");
        }
        return locations.toString();
    }

    @Override
    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext wac = findWebApplicationContext();
        if (wac == null) {
            ApplicationContext parent = AppContext.getApplicationContext();
            wac = createWebApplicationContext(parent);
        }

        onRefresh(wac);

        // Publish the context as a servlet context attribute.
        String attrName = getServletContextAttributeName();
        getServletContext().setAttribute(attrName, wac);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() +
                    "' as ServletContext attribute with name [" + attrName + "]");
        }

        return wac;
    }
}