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
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.IntegerListTypeFactory;

import java.util.List;

/**
 * Desktop UI configuration parameters.
 * <p/>
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Source(type = SourceType.APP)
@Prefix("cuba.desktop.")
public interface DesktopConfig extends Config {

    /**
     * List of root directories where resources (icons, images, theme config) are located.
     * Resources are local to each desktop theme.
     * For each root directory must exist subdirectory with name of current theme.
     *
     * @return list of root resource directories
     */
    @Default("com/haulmont/cuba/desktop/res")
    String getResourceLocations();

    @DefaultInt(25)
    int getMainTabCaptionLength();

    /**
     * Desktop theme name.
     * Theme configuration file is located as <code>${resourceLocation}/${themeName}/${themeName}.xml</code>.
     *
     * @return desktop theme name
     */
    @Default("nimbus")
    String getTheme();

    @DefaultBoolean(false)
    boolean isDialogNotificationsEnabled();

    @Factory(factory = IntegerListTypeFactory.class)
    @Default("6 8 10 12 14 16 18 20 22 24 28 32 36 48 54 60 72")
    List<Integer> getAvailableFontSizes();

    /**
     * @return true if application should change time zone to that which is used by server
     */
    @DefaultBoolean(true)
    boolean isUseServerTimeZone();
}
