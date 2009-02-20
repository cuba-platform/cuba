/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2009 18:18:22
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import javax.ejb.Local;
import java.util.UUID;
import java.io.Serializable;

@Local
public interface UserSessionService
{
    String JNDI_NAME = "cuba/security/UserSessionService";

    void putSessionAttribute(UUID sessionId, String name, Serializable value);
}
