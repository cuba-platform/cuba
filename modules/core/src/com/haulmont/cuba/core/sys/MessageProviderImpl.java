/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.03.2009 11:40:42
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.SecurityProvider;

import java.util.Locale;

public class MessageProviderImpl extends AbstractMessageProvider
{
    @Override
    protected Locale getUserLocale() {
        return SecurityProvider.checkCurrentUserSession() ?
                    SecurityProvider.currentUserSession().getLocale() :
                    Locale.getDefault();
    }

    @Override
    protected String searchRemotely(String pack, String key, Locale locale) {
        return null;
    }
}
