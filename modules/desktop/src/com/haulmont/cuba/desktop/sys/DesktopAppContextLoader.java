/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.sys;

import com.google.common.base.Splitter;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.*;
import com.haulmont.cuba.gui.AppConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * {@link AppContext} loader of the desktop client application.
 *
 */
public class DesktopAppContextLoader extends AbstractAppContextLoader {

    public static final String HOME_DIR_SYS_PROP = "cuba.desktop.home";

    public static final Pattern SEPARATOR_PATTERN = Pattern.compile("\\s");

    public static final String APP_COMPONENTS_SYS_PROP = "cuba.appComponents";

    public static final String APP_PROPERTIES_CONFIG_SYS_PROP = "cuba.appPropertiesConfig";

    private String defaultAppComponents;
    private String defaultAppPropertiesConfig;
    private String[] args;

    private Logger log = LoggerFactory.getLogger(DesktopAppContextLoader.class);

    public DesktopAppContextLoader(String defaultAppComponents, String defaultAppPropertiesConfig, String[] args) {
        this.defaultAppComponents = defaultAppComponents;
        this.defaultAppPropertiesConfig = defaultAppPropertiesConfig;
        this.args = args;
    }

    @Override
    protected String getBlock() {
        return "desktop";
    }

    public void load() {
        AppContext.Internals.setSecurityContextHolder(new SingleSecurityContextHolder());

        initAppComponents();
        initAppProperties();
        afterInitAppProperties();

        beforeInitAppContext();
        initAppContext();
        afterInitAppContext();

        initEnvironment();

        AppContext.Internals.startContext();
        log.info("AppContext initialized");
    }

    protected void initEnvironment() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        String tempPath = configuration.getConfig(GlobalConfig.class).getTempDir();
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

    private void initAppComponents() {
        String block = getBlock();

        String appComponentsProp = System.getProperty(APP_COMPONENTS_SYS_PROP);
        if (StringUtils.isBlank(appComponentsProp))
            appComponentsProp = defaultAppComponents;

        AppComponents appComponents;
        if (StringUtils.isEmpty(appComponentsProp)) {
            appComponents = new AppComponents(block);
        } else {
            List<String> compNames = Splitter.on(SEPARATOR_PATTERN).omitEmptyStrings().splitToList(appComponentsProp);
            appComponents = new AppComponents(compNames, block);
        }
        AppContext.Internals.setAppComponents(appComponents);
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
                    Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8.name());
                    properties.load(reader);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(stream);
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
            AppContext.setProperty((String) key, value.trim());
        }

        List<String> list = new ArrayList<>();
        for (String key : AppContext.getPropertyNames()) {
            list.add(key + "=" + AppContext.getProperty(key));
        }
        Collections.sort(list);
        log.info(new StrBuilder("AppProperties:\n").appendWithSeparators(list, "\n").toString());
    }
}