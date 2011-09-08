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
 * Global UUID provider for static context. Must be used everywhere instead of <code>UUID.randomUUID()</code>
 * Consider use of {@link UuidSource} directly.
 */
public abstract class UuidProvider
{
    public static UUID createUuid() {
        if (AppContext.getApplicationContext() == null)
            return UUID.randomUUID();

        return AppContext.getBean(UuidSource.NAME, UuidSource.class).createUuid();
    }
}
