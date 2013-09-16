/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.MessageProvider;

import javax.annotation.ManagedBean;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(LocalizedMessageService.NAME)
public class LocalizedMessageServiceBean implements LocalizedMessageService {

    @Override
    public String getMessage(String pack, String key, Locale locale) {
        return MessageProvider.getMessage(pack, key, locale);
    }
}
