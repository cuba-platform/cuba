/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SingleSecurityContextHolder;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.config.WindowConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopAppContextLoader {

    public static final String HOME_DIR_SYS_PROP = "cuba.desktop.home";

    public static final String APP_PROPERTIES_CONFIG_SYS_PROP = "cuba.appPropertiesConfig";

    public static final String SPRING_CONTEXT_CONFIG = "cuba.springContextConfig";

    private String defaultAppPropertiesConfig;

    private Log log = LogFactory.getLog(DesktopAppContextLoader.class);

    public DesktopAppContextLoader(String defaultAppPropertiesConfig) {
        this.defaultAppPropertiesConfig = defaultAppPropertiesConfig;
    }

    public void load() {
        AppContext.setSecurityContextHolder(new SingleSecurityContextHolder());

        initAppProperties();
        initAppContext();
        initLocalization();
        initServiceLocator();

        AppContext.startContext();
        log.info("AppContext initialized");
    }

    private void initLocalization() {
        String mp = AppContext.getProperty(AppConfig.MESSAGES_PACK_PROP);
        MessageUtils.setMessagePack(mp);

        for (Locale locale : ConfigProvider.getConfig(GlobalConfig.class).getAvailableLocales().values()) {
            Datatypes.setFormatStrings(
                    locale,
                    new FormatStrings(
                            MessageProvider.getMessage(mp, "numberDecimalSeparator", locale).charAt(0),
                            MessageProvider.getMessage(mp, "numberGroupingSeparator", locale).charAt(0),
                            MessageProvider.getMessage(mp, "integerFormat", locale),
                            MessageProvider.getMessage(mp, "doubleFormat", locale),
                            MessageProvider.getMessage(mp, "dateFormat", locale),
                            MessageProvider.getMessage(mp, "dateTimeFormat", locale),
                            MessageProvider.getMessage(mp, "trueString", locale),
                            MessageProvider.getMessage(mp, "falseString", locale)
                    )
            );
        }
    }

    private void initAppProperties() {
        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.DESKTOP.toString());
        AppContext.setProperty(AppConfig.IMPL_PROP, DesktopAppConfig.class.getName());
        AppContext.setProperty(AppConfig.WINDOW_CONFIG_IMPL_PROP, WindowConfig.class.getName());

        String appPropertiesConfig = System.getProperty(APP_PROPERTIES_CONFIG_SYS_PROP);
        if (StringUtils.isBlank(appPropertiesConfig))
            appPropertiesConfig = defaultAppPropertiesConfig;

        final Properties properties = new Properties();

        StrTokenizer tokenizer = new StrTokenizer(appPropertiesConfig);
        for (String str : tokenizer.getTokenArray()) {
            InputStream stream = null;
            try {
                stream = getClass().getResourceAsStream(str);
                if (stream != null) {
                    properties.load(stream);
                }
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

        ApplicationContext appContext = new ClassPathXmlApplicationContext(configProperty);
        AppContext.setApplicationContext(appContext);
    }

    private void initServiceLocator() {
        ServiceLocator.setImplClass(ServiceLocatorImpl.class);
    }
}
