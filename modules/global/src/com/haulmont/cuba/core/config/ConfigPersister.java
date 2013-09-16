/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface ConfigPersister
{
    String getProperty(SourceType sourceType, String name);

    void setProperty(SourceType sourceType, String name, String value);
}
