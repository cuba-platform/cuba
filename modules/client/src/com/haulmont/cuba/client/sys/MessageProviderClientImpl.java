/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 18:09
 *
 * $Id$
 */
package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AbstractMessageProvider;
import com.haulmont.cuba.security.global.UserSession;

import java.util.Locale;

public class MessageProviderClientImpl extends AbstractMessageProvider {

    @Override
    protected Locale getUserLocale() {
        UserSession userSession = UserSessionProvider.getUserSession();
        return userSession != null ? userSession.getLocale() : Locale.getDefault();
    }
}
