/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;

import java.util.UUID;

/**
 * Provides static method to create UUIDs.
 * Consider direct use of {@link UuidSource} via DI or <code>AppBeans.get(UuidSource.class)</code>.
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class UuidProvider {

    public static UUID createUuid() {
        if (AppContext.getApplicationContext() == null)
            return UUID.randomUUID();

        return AppBeans.get(UuidSource.NAME, UuidSource.class).createUuid();
    }
}
