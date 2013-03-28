/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
