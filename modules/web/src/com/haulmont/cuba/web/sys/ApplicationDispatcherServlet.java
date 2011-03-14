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

import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationDispatcherServlet extends DispatcherServlet {
    private static final long serialVersionUID = -4884517938479910144L;

    private static final String CONTEXT_CONFIG_PARAM = "contextConfig";

    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {
        super.postProcessWebApplicationContext(wac);

        String s = getServletConfig().getInitParameter(CONTEXT_CONFIG_PARAM);
        if (s != null) {
            StrTokenizer tokenizer = new StrTokenizer(s);
            String[] configLocations = tokenizer.getTokenArray();

            wac.setConfigLocations(configLocations);
        }
    }

    @Override
    public Class getContextClass() {
        return ClassPathXmlWebApplicationContext.class;
    }
}
