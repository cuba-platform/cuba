/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal;

import com.haulmont.cuba.security.global.LoginException;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConnectionListener {

    void connectionStateChanged(Connection connection) throws LoginException;
}
