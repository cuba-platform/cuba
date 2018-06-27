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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import com.haulmont.cuba.core.sys.persistence.EclipseLinkCustomizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.commons.text.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * Base class for {@link AppContext} loaders.
 *
 */
public abstract class AbstractAppContextLoader {

    public static final String SPRING_CONTEXT_CONFIG = "cuba.springContextConfig";

    private static final Logger log = LoggerFactory.getLogger(AbstractAppContextLoader.class);

    protected abstract String getBlock();

    protected void afterInitAppProperties() {
    }

    protected void beforeInitAppContext() {
        EclipseLinkCustomizer.initTransientCompatibleAnnotations();
    }

    protected void initAppContext() {
        String configProperty = AppContext.getProperty(SPRING_CONTEXT_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + SPRING_CONTEXT_CONFIG + " application property");
        }

        StringTokenizer tokenizer = new StringTokenizer(configProperty);
        String[] locations = tokenizer.getTokenArray();
        replaceLocationsFromConf(locations);

        ApplicationContext appContext = createApplicationContext(locations);
        AppContext.Internals.setApplicationContext(appContext);

        Events events = AppBeans.get(Events.NAME);
        events.publish(new AppContextInitializedEvent(appContext));

        log.debug("AppContext initialized");
    }

    protected void replaceLocationsFromConf(String[] locations) {
        String confDirProp = AppContext.getProperty("cuba.confDir");
        if (confDirProp == null)
            throw new IllegalStateException("cuba.confDir app property is not set");
        File confDir = new File(confDirProp);
        for (int i = 0; i < locations.length; i++) {
            String location = locations[i];
            if (ResourceUtils.isUrl(location))
                continue;
            if (location.startsWith("/"))
                location = location.substring(1);
            File file = new File(confDir, location);
            if (file.exists()) {
                locations[i] = file.toURI().toString();
            }
        }
    }

    protected ApplicationContext createApplicationContext(String[] locations) {
        return new CubaClassPathXmlApplicationContext(locations);
    }

    protected void afterInitAppContext() {
    }
}