/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Source(type = SourceType.APP)
@Prefix("cuba.desktop.")
public interface DesktopConfig extends Config {

    @Default("com/haulmont/cuba/desktop/res")
    String getResourceLocations();

    @DefaultInt(25)
    int getMainTabCaptionLength();
}
