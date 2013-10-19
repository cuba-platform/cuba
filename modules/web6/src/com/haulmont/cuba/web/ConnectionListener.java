/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.LoginException;

/**
 * Listener of connection events. See {@link com.haulmont.cuba.web.Connection}.
 */
public interface ConnectionListener
{
    void connectionStateChanged(Connection connection) throws LoginException;
}
