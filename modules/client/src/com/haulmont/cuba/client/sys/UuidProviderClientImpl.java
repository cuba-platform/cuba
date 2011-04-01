/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 18:41
 *
 * $Id$
 */
package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.global.UuidProvider;

import java.util.UUID;

public class UuidProviderClientImpl extends UuidProvider {

    @Override
    protected UUID __createUuid() {
        return UUID.randomUUID();
    }
}
