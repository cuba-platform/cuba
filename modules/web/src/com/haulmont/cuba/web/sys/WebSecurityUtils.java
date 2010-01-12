/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2009 15:07:09
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;

import java.util.UUID;

public class WebSecurityUtils {

    public static void setSecurityAssociation(String userName, UUID sessionId) {
        AppContext.setSecurityContext(new SecurityContext(userName, null, sessionId));
    }

    public static void clearSecurityAssociation() {
        AppContext.setSecurityContext(null);
    }
}
