/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 23.12.2009 15:02:34
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebAppContextLoader implements ServletContextListener {

    public static final String SPRING_CONTEXT_CONFIG = "cuba.springContextConfig";

    public static final String APP_PROPS_CONFIG_PARAM = "appPropertiesConfig";

    public static final String APP_PROPS_PARAM = "appProperties";

    private static Log log = LogFactory.getLog(WebAppContextLoader.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ServletContext sc = servletContextEvent.getServletContext();

            initAppProperties(sc);
            MessageUtils.setMessagePack(AppContext.getProperty(AppConfig.MESSAGES_PACK_PROP));

            initAppContext();
            initServiceLocator();

            AppContext.startContext();
            log.info("AppContext initialized");
        } catch (Throwable e) {
            log.error("Error initializing application", e);
            throw new RuntimeException(e);
        }
    }

    private void initAppProperties(ServletContext sc) {
        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
        AppContext.setProperty(AppConfig.WINDOW_CONFIG_IMPL_PROP, "com.haulmont.cuba.web.WebWindowConfig");

        // get properties from web.xml
        String appProperties = sc.getInitParameter(APP_PROPS_PARAM);
        if (appProperties != null) {
            StrTokenizer tokenizer = new StrTokenizer(appProperties);
            for (String str : tokenizer.getTokenArray()) {
                int i = str.indexOf("=");
                if (i < 0)
                    continue;
                String name = StringUtils.substring(str, 0, i);
                String value = StringUtils.substring(str, i+1);
                if (!StringUtils.isBlank(name)) {
                    AppContext.setProperty(name, value);
                }
            }
        }

        // get properties from app.properties
        String propsConfigName = sc.getInitParameter(APP_PROPS_CONFIG_PARAM);
        if (propsConfigName == null)
            throw new IllegalStateException(APP_PROPS_CONFIG_PARAM + " servlet context parameter not defined");

        final Properties properties = new Properties();

        StrTokenizer tokenizer = new StrTokenizer(propsConfigName);
        for (String str : tokenizer.getTokenArray()) {
            File file = new File(str);
            if (file.exists()) {
                InputStream stream = null;
                try {
                    stream = new FileInputStream(file);
                    properties.load(stream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (stream != null) stream.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }

        StrSubstitutor substitutor = new StrSubstitutor(new StrLookup() {
            @Override
            public String lookup(String key) {
                String subst = properties.getProperty(key);
                return subst != null ? subst : System.getProperty(key);
            }
        });
        for (Object key : properties.keySet()) {
            String value = substitutor.replace(properties.getProperty((String) key));
            AppContext.setProperty((String) key, value);
        }
    }

    private void initAppContext() {
        String configProperty = AppContext.getProperty(SPRING_CONTEXT_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + SPRING_CONTEXT_CONFIG + " application property");
        }

        String baseDir = AppContext.getProperty("cuba.confDir");

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        String[] tokenArray = tokenizer.getTokenArray();
        String[] locations = new String[tokenArray.length];
        for (int i = 0; i < tokenArray.length; i++) {
            locations[i] = baseDir + "/" + tokenArray[i];
        }

        ApplicationContext appContext = new FileSystemXmlApplicationContext(locations);
        AppContext.setApplicationContext(appContext);
    }

    private void initServiceLocator() {
        ServiceLocator.setImplClass(ServiceLocatorImpl.class);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        AppContext.stopContext();
        AppContext.setApplicationContext(null);
    }
}
