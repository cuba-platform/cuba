/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultString;

/**
 * Portal configuration parameters interface.
 *
 * @author artamonov
 * @version $Id$
 */
@Source(type = SourceType.APP)
public interface PortalConfig extends Config {

    @Property("cuba.portal.anonymousUserLogin")
    String getAnonymousUserLogin();

    @Property("cuba.trustedClientPassword")
    String getTrustedClientPassword();

    @Property("cuba.portal.theme")
    @DefaultString("default")
    String getTheme();
}