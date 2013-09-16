/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AbstractAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SingleSecurityContextHolder;
import com.haulmont.cuba.gui.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * {@link AppContext} loader of the desktop client application.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DesktopAppContextLoader extends AbstractAppContextLoader {

    public static final String HOME_DIR_SYS_PROP = "cuba.desktop.home";

    public static final String APP_PROPERTIES_CONFIG_SYS_PROP = "cuba.appPropertiesConfig";

    private String defaultAppPropertiesConfig;
    private String[] args;

    private Log log = LogFactory.getLog(DesktopAppContextLoader.class);

    public DesktopAppContextLoader(String defaultAppPropertiesConfig, String[] args) {
        this.defaultAppPropertiesConfig = defaultAppPropertiesConfig;
        this.args = args;
    }

    public void load() {
        AppContext.setSecurityContextHolder(new SingleSecurityContextHolder());

        initAppProperties();
        afterInitAppProperties();

        beforeInitAppContext();
        initAppContext();
        afterInitAppContext();

        initEnvironment();

        AppContext.startContext();
        log.info("AppContext initialized");
    }

    protected void initEnvironment() {
        String tempPath = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class).getTempDir();
        File tempDir = new File(tempPath);
        if (!tempDir.exists()) {
            try {
                boolean result = tempDir.mkdirs();
                if (!result)
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, tempPath);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected void initAppProperties() {
        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.DESKTOP.toString());

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

        for (String arg : args) {
            arg = arg.trim();
            int pos = arg.indexOf('=');
            if (pos > 0) {
                String key = arg.substring(0, pos);
                String value = arg.substring(pos + 1);
                properties.setProperty(key, value);
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

        List<String> list = new ArrayList<String>();
        for (String key : AppContext.getPropertyNames()) {
            list.add(key + "=" + AppContext.getProperty(key));
        }
        Collections.sort(list);
        log.info(new StrBuilder("AppProperties:\n").appendWithSeparators(list, "\n"));
    }
}
