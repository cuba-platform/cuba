/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:29:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.SecurityProvider;

public class SecurityProviderImpl extends SecurityProvider
{
    protected String __currentUserLogin() {
        return "admin";
    }
}
