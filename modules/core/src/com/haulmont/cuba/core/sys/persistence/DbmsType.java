/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

/**
 * INTERNAL.
 * System level wrapper around DBMS-related application properties.
 *
 * <p>For data conversion on the middleware use {@link DbTypeConverter} obtained from
 * {@link com.haulmont.cuba.core.Persistence} bean.
 * <p>If your client code needs to know what DBMS is currently in use, call {@code getDbmsType()} and
 * {@code getDbmsVersion()} methods of {@link com.haulmont.cuba.core.app.PersistenceManagerService}.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DbmsType {

    public static String getType() {
        String id = AppContext.getProperty("cuba.dbmsType");
        if (StringUtils.isBlank(id))
            throw new IllegalStateException("cuba.dbmsType is not set");
        return id;
    }

    public static String getVersion() {
        return StringUtils.trimToEmpty(AppContext.getProperty("cuba.dbmsVersion"));
    }
}