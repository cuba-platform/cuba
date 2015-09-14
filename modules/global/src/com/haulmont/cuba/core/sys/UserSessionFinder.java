/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.security.global.UserSession;

import java.util.UUID;

/**
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public interface UserSessionFinder {

    UserSession findSession(UUID sessionId);
}
