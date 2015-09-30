/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.Messages;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(LocalizedMessageService.NAME)
public class LocalizedMessageServiceBean implements LocalizedMessageService {

    @Inject
    protected Messages messages;

    @Override
    public String getMessage(String pack, String key, Locale locale) {
        return messages.getMessage(pack, key, locale);
    }
}
