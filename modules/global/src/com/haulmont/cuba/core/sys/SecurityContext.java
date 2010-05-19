/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.12.2009 16:58:36
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import java.util.UUID;

public class SecurityContext {

    private final String user;
    private final String password;
    private final UUID sessionId;

    public SecurityContext(UUID sessionId) {
        this(null, null, sessionId);
    }

    public SecurityContext(String user, String password, UUID sessionId) {
        this.user = user;
        this.sessionId = sessionId;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public String getUser() {
        return user;
    }
}
