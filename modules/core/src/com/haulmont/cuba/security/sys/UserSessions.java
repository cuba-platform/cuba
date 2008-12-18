/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:13:18
 *
 * $Id$
 */
package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.security.global.UserSession;

import java.util.UUID;

public interface UserSessions
{
    void add(UserSession session);

    void remove(UserSession session);

    UserSession get(UUID id);
}
