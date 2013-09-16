/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys.security;

import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public interface PortalSessionFactory {

    String NAME = "cuba_PortalSessionFactory";

    PortalSession createPortalSession(@Nullable UserSession userSession, @Nullable Locale locale);
}
