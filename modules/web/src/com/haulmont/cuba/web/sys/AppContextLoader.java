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

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.PersistenceConfigProcessor;
import com.haulmont.cuba.gui.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppContextLoader implements ServletContextListener {

    public static final String APP_CONTEXT_NAME_PARAM = "appContextConfigName";

    public static final String APP_PROPS_CONFIG_PARAM = "appPropertiesConfigName";

    public static final String APP_PROPS_PARAM = "appProperties";

    public static final String PERSISTENCE_CONFIG_NAME_PARAM = "persistenceConfigName";

    private Log log = LogFactory.getLog(AppContextLoader.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ServletContext sc = servletContextEvent.getServletContext();

            initAppProperties(sc);
            initPersistenceConfig(sc);
            initAppContext(sc);
            initServiceLocator(sc);

            AppContext.startContext();
        } catch (Exception e) {
            log.error("Error initializing application", e);
            throw new RuntimeException(e);
        }
    }

    private void initAppProperties(ServletContext sc) {
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

        final Properties properties;
        InputStream stream = sc.getResourceAsStream(propsConfigName);
        try {
            properties = new Properties();
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                //
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

    private void initPersistenceConfig(ServletContext sc) {
        String pcProperties = sc.getInitParameter(PERSISTENCE_CONFIG_NAME_PARAM);
        if (StringUtils.isBlank(pcProperties)) {
            throw new IllegalStateException(PERSISTENCE_CONFIG_NAME_PARAM + " context-param not found in web.xml");
        }

        StrTokenizer tokenizer = new StrTokenizer(pcProperties);
        PersistenceConfigProcessor processor = new PersistenceConfigProcessor();
        processor.setSourceFiles(tokenizer.getTokenList());

        String confPath = AppContext.getProperty("cuba.confDir");
        String webInfPath = new File(confPath).getParent();
        processor.setOutputFile(webInfPath + "/classes/persistence.xml");

        processor.create();
    }

    private void initAppContext(ServletContext sc) {
        String ctxConfigName = sc.getInitParameter(APP_CONTEXT_NAME_PARAM);
        if (ctxConfigName == null)
            throw new IllegalStateException(APP_CONTEXT_NAME_PARAM + " servlet context parameter not defined");

        StrTokenizer tokenizer = new StrTokenizer(ctxConfigName);
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(tokenizer.getTokenArray());
        AppContext.setApplicationContext(appContext);
    }

    private void initServiceLocator(ServletContext sc) {
        ServiceLocator.setImplClass(ServiceLocatorImpl.class);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        AppContext.setApplicationContext(null);
    }
}
