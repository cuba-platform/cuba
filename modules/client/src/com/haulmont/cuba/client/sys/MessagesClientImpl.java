/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.LocalizedMessageService;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AbstractMessages;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.net.SocketException;
import java.util.List;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Messages.NAME)
public class MessagesClientImpl extends AbstractMessages {

    @Inject
    protected LocalizedMessageService localizedMessageService;

    @Inject
    protected UserSessionSource userSessionSource;

    protected volatile boolean remoteSearch;

    protected ClientConfig clientConfig;

    @Inject
    @Override
    public void setConfiguration(Configuration configuration) {
        super.setConfiguration(configuration);
        clientConfig = configuration.getConfig(ClientConfig.class);
        remoteSearch = clientConfig.getRemoteMessagesSearchEnabled();
    }

    @Override
    protected Locale getUserLocale() {
        return userSessionSource.checkCurrentUserSession() ?
                userSessionSource.getUserSession().getLocale() :
                messageTools.getDefaultLocale();
    }

    @Override
    protected String searchRemotely(String pack, String key, Locale locale) {
        if (!remoteSearch || !AppContext.isStarted())
            return null;

        if (log.isTraceEnabled())
            log.trace("searchRemotely: " + pack + "/" + locale + "/" + key);

        StopWatch stopWatch = new Log4JStopWatch("Messages.searchRemotely");
        try {
            String message = localizedMessageService.getMessage(pack, key, locale);
            if (key.equals(message))
                return null;
            else
                return message;
        } catch (Exception e) {
            List list = ExceptionUtils.getThrowableList(e);
            for (Object throwable : list) {
                if (throwable instanceof SocketException) {
                    log.trace("searchRemotely: " + throwable);
                    return null; // silently ignore network errors
                }
            }
            throw (RuntimeException) e;
        } finally {
            stopWatch.stop();
        }
    }

    public boolean isRemoteSearch() {
        return remoteSearch;
    }

    public void setRemoteSearch(boolean remoteSearch) {
        this.remoteSearch = remoteSearch && clientConfig.getRemoteMessagesSearchEnabled();
    }
}
