/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.security.global.UserSession;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
@Component(PortalSessionFactory.NAME)
public class DefaultPortalSessionFactory implements PortalSessionFactory {

    @Inject
    protected AnonymousSessionHolder anonymousSessionHolder;

    @Override
    public PortalSession createPortalSession(@Nullable UserSession sourceUserSession, Locale locale) {
        if (sourceUserSession == null) {
            sourceUserSession = anonymousSessionHolder.getSession();
            return new AnonymousSession(sourceUserSession, locale);
        }
        return new PortalSession(sourceUserSession);
    }
}
