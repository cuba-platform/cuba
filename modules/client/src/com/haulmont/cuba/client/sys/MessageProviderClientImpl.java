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

import com.haulmont.cuba.core.app.LocalizedMessageService;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AbstractMessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.net.ConnectException;
import java.util.List;
import java.util.Locale;

public class MessageProviderClientImpl extends AbstractMessageProvider {

    @Override
    protected Locale getUserLocale() {
        UserSession userSession = UserSessionProvider.getUserSession();
        return userSession != null ? userSession.getLocale() : Locale.getDefault();
    }

    @Override
    protected String searchRemotely(String pack, String key, Locale locale) {
        if (!AppContext.isStarted())
            return null;

        try {
            LocalizedMessageService ms = AppContext.getBean(LocalizedMessageService.NAME);
            String message = ms.getMessage(pack, key, locale);
            if (key.equals(message))
                return null;
            else
                return message;
        } catch (Exception e) {
            List list = ExceptionUtils.getThrowableList(e);
            for (Object throwable : list) {
                if (throwable instanceof ConnectException)
                    return null; // silently ignore connection errors
            }
            throw (RuntimeException) e;
        }
    }
}
