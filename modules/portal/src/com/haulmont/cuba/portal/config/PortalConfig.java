/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultString;

/**
 * Portal configuration parameters interface used by the WEB layer.
 *
 * @author artamonov
 * @version $Id$
 */
@Source(type = SourceType.APP)
public interface PortalConfig extends Config {

    @Property("cuba.portal.trustedClientLogin")
    String getTrustedClientLogin();

    @Property("cuba.portal.trustedClientPassword")
    String getTrustedClientPassword();

    @Property("cuba.portal.defaultLocale")
    @DefaultString("en")
    String getDefaultLocale();
    void setDefaultLocale(String defaultLocale);

    @Property("cuba.portal.theme")
    @DefaultString("default")
    String getTheme();
}