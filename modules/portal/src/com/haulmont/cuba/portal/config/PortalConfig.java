/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultString;

/**
 * PortalConfiguration parameters interface used by the WEB layer.
 *
 * @author artamonov
 * @version $Id$
 */
@Source(type = SourceType.APP)
@Prefix("cuba.portal.")
public interface PortalConfig extends Config {

    String getMiddlewareLogin();

    String getMiddlewarePassword();

    @DefaultString("en")
    String getDefaultLocale();
    void setDefaultLocale(String defaultLocale);

    @DefaultString("default")
    String getTheme();

    @DefaultString("dd/MM/yyyy")
    String getDateFormatShort();
    void setDateFormatShort(String dateFormatShort);

    @DefaultString("dd/MM/yyyy HH:mm")
    String getDateFormatLong();
    void setDateFormatLong(String dateFormatLong);
}
