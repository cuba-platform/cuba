/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:52:00
 *
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.global.UuidProvider;

import java.util.UUID;

public class UuidProviderImpl extends UuidProvider
{
    protected UUID __createUuid() {
        // TODO KK: implement UUID version 1
        return UUID.randomUUID();
    }
}
