/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
