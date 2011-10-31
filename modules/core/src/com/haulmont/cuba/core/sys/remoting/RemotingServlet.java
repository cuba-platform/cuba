/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RemotingServlet extends DispatcherServlet {

    private static final long serialVersionUID = 4142366570614871805L;

    public static final String SPRING_CONTEXT_CONFIG = "cuba.remotingSpringContextConfig";

    private volatile boolean checkCompleted;

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

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkCompleted) {
            // Check correctness of some configuration parameters and log the warning if necesary
            GlobalConfig config = ConfigProvider.getConfig(GlobalConfig.class);
            StringBuilder sb = new StringBuilder();
            if (!request.getServerName().equals(config.getWebHostName())) {
                sb.append("***** cuba.webHostName=").append(config.getWebHostName())
                        .append(", actual=").append(request.getServerName()).append("\n");
            }
            if (request.getServerPort() != Integer.valueOf(config.getWebPort())) {
                sb.append("***** cuba.webPort=").append(config.getWebPort())
                        .append(", actual=").append(request.getServerPort()).append("\n");
            }
            String contextPath = request.getContextPath();
            if (contextPath.startsWith("/"))
                contextPath = contextPath.substring(1);
            if (!contextPath.equals(config.getWebContextName())) {
                sb.append("***** cuba.webContextName=").append(config.getWebContextName())
                        .append(", actual=").append(contextPath).append("\n");
            }
            if (sb.length() > 0) {
                sb.insert(0, "\n*****\n");
                sb.append("*****");
                LogFactory.getLog(getClass()).warn(" Invalid configuration parameters that may cause problems:" +
                        sb.toString()
                );
            }
            checkCompleted = true;
        }
        super.doService(request, response);
    }
}
