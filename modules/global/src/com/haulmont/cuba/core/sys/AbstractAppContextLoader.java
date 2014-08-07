/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Base class for {@link AppContext} loaders.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AbstractAppContextLoader {

    public static final String SPRING_CONTEXT_CONFIG = "cuba.springContextConfig";

    protected void afterInitAppProperties() {
        BaseUuidEntity.allowSetNotLoadedAttributes =
                Boolean.valueOf(AppContext.getProperty("cuba.allowSetNotLoadedAttributes"));
    }

    protected void beforeInitAppContext() {
    }

    protected void initAppContext() {
        String configProperty = AppContext.getProperty(SPRING_CONTEXT_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + SPRING_CONTEXT_CONFIG + " application property");
        }

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        String[] locations = tokenizer.getTokenArray();

        ClassPathXmlApplicationContext appContext = new CubaClassPathXmlApplicationContext();

        appContext.setConfigLocations(locations);
        appContext.setValidating(false);
        appContext.refresh();

        AppContext.setApplicationContext(appContext);
    }

    protected void afterInitAppContext() {
    }
}