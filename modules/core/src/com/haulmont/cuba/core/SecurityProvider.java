/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:27:17
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.impl.SecurityProviderImpl;

public abstract class SecurityProvider
{
    private static SecurityProvider instance;

    private static SecurityProvider getInstance() {
        if (instance == null) {
            instance = new SecurityProviderImpl();
        }
        return instance;
    }

    public static String currentUserLogin() {
        return getInstance().__currentUserLogin();
    }

    protected abstract String __currentUserLogin();
}
