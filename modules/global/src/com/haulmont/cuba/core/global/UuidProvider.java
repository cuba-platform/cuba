/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:49:00
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;

import java.util.UUID;

/**
 * Global UUID provider. Must be used everywhere instead of <code>UUID.randomUUID()</code>
 */
public abstract class UuidProvider
{
    private static UuidProvider getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_UuidProvider", UuidProvider.class);
    }

    public static UUID createUuid() {
        if (AppContext.getApplicationContext() == null)
            return UUID.randomUUID();

        return getInstance().__createUuid();
    }

    protected abstract UUID __createUuid();
}
