/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 11.03.2011 17:42:02
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class CubaDispatcherServlet extends DispatcherServlet {

    private static final long serialVersionUID = -4884517938479910144L;

    public static final String SPRING_CONTEXT_CONFIG = "cuba.dispatcherSpringContextConfig";

    @Override
    public String getContextConfigLocation() {
        String configProperty = AppContext.getProperty(SPRING_CONTEXT_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + SPRING_CONTEXT_CONFIG + " application property");
        }
        return configProperty;
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

//    @Override
//    protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {
//        super.postProcessWebApplicationContext(wac);
//
//        String s = getServletConfig().getInitParameter(CONTEXT_CONFIG_PARAM);
//        if (s != null) {
//            StrTokenizer tokenizer = new StrTokenizer(s);
//            String[] configLocations = tokenizer.getTokenArray();
//
//            wac.setConfigLocations(configLocations);
//        }
//    }
//
//    @Override
//    public Class getContextClass() {
//        return ClassPathXmlWebApplicationContext.class;
//    }
}
