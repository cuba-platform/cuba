/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.security.global.LoginException;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConnectionListener {

    void connectionStateChanged(Connection connection) throws LoginException;
}
