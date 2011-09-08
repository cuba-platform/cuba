/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Messages.NAME)
public class MessagesImpl extends AbstractMessages {

    @Inject
    private UserSessionSource userSessionSource;

    @Override
    protected Locale getUserLocale() {
        return userSessionSource.checkCurrentUserSession() ?
                    userSessionSource.getUserSession().getLocale() :
                    Locale.getDefault();
    }

    @Override
    protected String searchRemotely(String pack, String key, Locale locale) {
        return null;
    }
}
