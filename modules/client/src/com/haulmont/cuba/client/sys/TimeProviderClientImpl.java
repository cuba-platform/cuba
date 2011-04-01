/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 18:38
 *
 * $Id$
 */
package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.global.TimeProvider;

import java.util.Date;

public class TimeProviderClientImpl extends TimeProvider {

    @Override
    protected Date __currentTimestamp() {
        return new Date();
    }
}
