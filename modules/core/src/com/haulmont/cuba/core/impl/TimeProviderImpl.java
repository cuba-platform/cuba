/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 16:14:49
 *
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.TimeProvider;

import java.util.Date;

public class TimeProviderImpl extends TimeProvider
{
    protected Date __currentTimestamp() {
        return new Date();
    }
}
