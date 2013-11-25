/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Messages.NAME)
public class MessagesImpl extends AbstractMessages {

    @Inject
    private UserSessionSource userSessionSource;

    @Override
    protected Locale getUserLocale() {
        return userSessionSource.checkCurrentUserSession() ?
                userSessionSource.getUserSession().getLocale() :
                messageTools.getDefaultLocale();
    }

    @Override
    protected String searchRemotely(String pack, String key, Locale locale) {
        return null;
    }
}
