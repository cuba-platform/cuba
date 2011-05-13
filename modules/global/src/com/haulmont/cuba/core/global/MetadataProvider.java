/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 11:57:54
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Entry point to the metadata functionality.<br>
 * Use static methods.
 */
public abstract class MetadataProvider
{
    public static final String METADATA_CONFIG = "cuba.metadataConfig";
    protected static final String DEFAULT_METADATA_CONFIG = "classpath:cuba-metadata.xml";

    private static MetadataProvider getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_MetadataProvider", MetadataProvider.class);
    }

    /**
     * Get current metadata session
     */
    public static Session getSession() {
        return getInstance().__getSession();
    }

    /**
     * Get the view repository
     */
    public static ViewRepository getViewRepository() {
        return getInstance().__getViewRepository();
    }

    public static Map<Class, Class> getReplacedEntities() {
        return getInstance().__getReplacedEntities();
    }

    /**
     * Get the location of non-persistent metadata descriptor
     */
    public static String getMetadataConfig() {
        String xmlPath = AppContext.getProperty(METADATA_CONFIG);
        if (StringUtils.isBlank(xmlPath))
            xmlPath = DEFAULT_METADATA_CONFIG;
        return xmlPath;
    }

    protected abstract Session __getSession();
    protected abstract ViewRepository __getViewRepository();
    protected abstract Map<Class,Class> __getReplacedEntities();
}
