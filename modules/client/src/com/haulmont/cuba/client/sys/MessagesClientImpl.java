/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.app.LocalizedMessageService;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AbstractMessages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.net.ConnectException;
import java.util.List;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Messages.NAME)
public class MessagesClientImpl extends AbstractMessages {

    @Inject
    private LocalizedMessageService localizedMessageService;

    @Override
    protected Locale getUserLocale() {
        UserSession userSession = UserSessionProvider.getUserSession();
        return userSession != null ? userSession.getLocale() : Locale.getDefault();
    }

    @Override
    protected String searchRemotely(String pack, String key, Locale locale) {
        if (!AppContext.isStarted())
            return null;

        if (log.isTraceEnabled())
            log.trace("searchRemotely: " + pack + "/" + locale + "/" + key);

        try {
            String message = localizedMessageService.getMessage(pack, key, locale);
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
