/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.04.2009 15:24:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.client.UserSessionClient;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;

public class WebUserSessionClient extends UserSessionClient
{
    protected UserSession __getUserSession() {
        return App.getInstance().getConnection().getSession();
    }
}
