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
import org.apache.commons.lang.StringUtils;

/**
 * Entry point to the metadata functionality.<br>
 * Use static methods.
 */
public abstract class MetadataProvider
{
    public static final String IMPL_PROP = "cuba.MetadataProvider.impl";
    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.MetadataProviderImpl";

    public static final String METADATA_XML = "cuba.MetadataXml";
    protected static final String DEFAULT_METADATA_XML = "META-INF/cuba-metadata.xml";

    private static MetadataProvider instance;

    private static MetadataProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (MetadataProvider) implClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
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

    /**
     * Get the location of non-persistent metadata descriptor
     */
    public static String getMetadataXmlPath() {
        String xmlPath = System.getProperty(METADATA_XML);
        if (StringUtils.isBlank(xmlPath))
            xmlPath = DEFAULT_METADATA_XML;
        return xmlPath;
    }

    protected abstract Session __getSession();
    protected abstract ViewRepository __getViewRepository();
}
