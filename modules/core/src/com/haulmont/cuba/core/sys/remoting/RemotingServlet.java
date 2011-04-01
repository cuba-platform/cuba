/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RemotingServlet extends DispatcherServlet {

    private static final long serialVersionUID = 4142366570614871805L;

    public static final String SPRING_CONTEXT_CONFIG = "cuba.remotingSpringContextConfig";

    @Override
    public String getContextConfigLocation() {
        String configProperty = AppContext.getProperty(SPRING_CONTEXT_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + SPRING_CONTEXT_CONFIG + " application property");
        }

        String baseDir = AppContext.getProperty("cuba.confDir");

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        String[] tokenArray = tokenizer.getTokenArray();
        StringBuilder locations = new StringBuilder();
        for (String token : tokenArray) {
            locations.append("file:").append(baseDir).append("/").append(token);
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
